package com.apzda.cloud.uc.security.authentication;

import cn.hutool.crypto.digest.DigestUtil;
import com.apzda.cloud.audit.aop.AuditLog;
import com.apzda.cloud.captcha.error.NeedCaptcha;
import com.apzda.cloud.captcha.helper.CaptchaHelper;
import com.apzda.cloud.config.exception.SettingUnavailableException;
import com.apzda.cloud.config.service.SettingService;
import com.apzda.cloud.gsvc.core.GsvcContextHolder;
import com.apzda.cloud.gsvc.error.ServiceError;
import com.apzda.cloud.gsvc.infra.TempStorage;
import com.apzda.cloud.gsvc.security.exception.AuthenticationError;
import com.apzda.cloud.gsvc.security.token.JwtAuthenticationToken;
import com.apzda.cloud.gsvc.security.userdetails.UserDetailsMeta;
import com.apzda.cloud.gsvc.security.userdetails.UserDetailsMetaRepository;
import com.apzda.cloud.uc.domain.entity.Oauth;
import com.apzda.cloud.uc.domain.service.UserManager;
import com.apzda.cloud.uc.security.AuthTempData;
import com.apzda.cloud.uc.setting.UcSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * @author fengz
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private final UserManager userManager;

    private final UserDetailsService userDetailsService;

    private final UserDetailsMetaRepository userDetailsMetaRepository;

    private final PasswordEncoder passwordEncoder;

    private final CaptchaHelper captchaHelper;

    private final SettingService settingService;

    private final TempStorage tempStorage;

    @Override
    @AuditLog(activity = "login", template = "{} authenticated successfully", errorTpl = "{} authenticated failure: {}",
            args = { "#authentication.principal", "#throwExp?.message" })
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("[{}] authenticate JwtAuthenticationToken: {}", GsvcContextHolder.getRequestId(),
                authentication.getPrincipal());

        val credentials = authentication.getCredentials();
        val principal = authentication.getPrincipal();
        if (Objects.isNull(principal) || StringUtils.isBlank((String) principal)) {
            throw new UsernameNotFoundException("username is blank");
        }
        val username = (String) principal;
        log.debug("开始用户/密码认证: {}", username);
        UcSetting ucSetting;
        try {
            ucSetting = settingService.load(UcSetting.class);
        }
        catch (SettingUnavailableException e) {
            log.error("无法加载用户中心配置: {}", e.getMessage());
            throw new AuthenticationError(ServiceError.SERVICE_UNAVAILABLE);
        }
        val data = load(username);
        // 验证码
        validateCaptcha(data, ucSetting);

        val userDetails = userDetailsService.loadUserByUsername(username);
        val password = userDetails.getPassword();

        UserDetailsMeta.checkUserDetails(userDetails);

        if (passwordEncoder.matches((CharSequence) credentials, password)) {
            val userDetailsMeta = userDetailsMetaRepository.create(userDetails);
            val authed = JwtAuthenticationToken.authenticated(userDetailsMeta, password);
            // bookmark: Clear Authorities, cause to reload authorities from
            // UserDetailsMetaService
            log.trace("[{}] Clear user's authorities and last login time: {}", GsvcContextHolder.getRequestId(),
                    username);
            userDetailsMeta.remove(UserDetailsMeta.AUTHORITY_META_KEY);
            userDetailsMeta.set(authed.deviceAwareMetaKey(UserDetailsMeta.LOGIN_TIME_META_KEY), 0L);

            log.trace("[{}] Reset auth temp data: {}", GsvcContextHolder.getRequestId(), username);
            data.setErrorCnt(0);
            data.setNeedCaptcha(false);
            save(username, data);

            val oauth = new Oauth();
            oauth.setOpenId(username);
            oauth.setUnionId(username);
            oauth.setProvider(Oauth.SIMPLE);
            userManager.onAuthenticated(authed, oauth);

            return authed;
        }

        if (ucSetting.getThresholdForCaptcha() >= 0) {
            data.setErrorCnt(data.getErrorCnt() + 1);
            if (data.getErrorCnt() >= ucSetting.getThresholdForCaptcha()) {
                data.setNeedCaptcha(true);
            }
            save(username, data);
            if (data.isNeedCaptcha()) {
                throw new AuthenticationError(new NeedCaptcha());
            }
        }
        throw new AuthenticationError(ServiceError.USER_PWD_INCORRECT);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void validateCaptcha(@NonNull AuthTempData data, @NonNull UcSetting ucSetting) {
        val threshold = ucSetting.getThresholdForCaptcha();
        if (threshold == -1 || threshold > 0 && !data.isNeedCaptcha()) {
            return;
        }

        captchaHelper.validate();
    }

    @NonNull
    private AuthTempData load(@NonNull String username) {
        val id = "auth.tmp." + DigestUtil.md5Hex(username);
        val data = tempStorage.load(id, AuthTempData.class);
        return data.orElse(new AuthTempData());
    }

    private void save(@NonNull String username, @NonNull AuthTempData data) {
        val id = "auth.tmp." + DigestUtil.md5Hex(username);
        try {
            tempStorage.save(id, data);
        }
        catch (Exception e) {
            log.warn("Cannot save Temp Data for {} - {}", username, data);
        }
    }

}
