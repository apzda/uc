/*
 * Copyright (C) 2023-2023 Fengz Ning (windywany@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.apzda.cloud.uc.config;

import com.apzda.cloud.captcha.helper.CaptchaHelper;
import com.apzda.cloud.config.service.SettingService;
import com.apzda.cloud.gsvc.infra.TempStorage;
import com.apzda.cloud.gsvc.security.authentication.DeviceAwareAuthenticationProcessingFilter;
import com.apzda.cloud.gsvc.security.token.JwtTokenCustomizer;
import com.apzda.cloud.gsvc.security.userdetails.UserDetailsMetaRepository;
import com.apzda.cloud.uc.domain.service.UserManager;
import com.apzda.cloud.uc.mapper.JwtTokenMapper;
import com.apzda.cloud.uc.security.UserDetailsServiceImpl;
import com.apzda.cloud.uc.security.authentication.DefaultAuthenticationProvider;
import com.apzda.cloud.uc.security.filter.UsernameAndPasswordFilter;
import com.apzda.cloud.uc.security.token.TokenCustomizer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Configuration(proxyBeanMethods = false)
@Slf4j
@EnableConfigurationProperties(UCenterConfigProperties.class)
public class UCenterConfig {

    @Configuration
    static class MvcConfigure implements WebMvcConfigurer {

    }

    @Configuration(proxyBeanMethods = false)
    static class SecurityConfigure {

        @Bean
        UserDetailsService userDetailsService(UserManager userManager,
                UserDetailsMetaRepository userDetailsMetaRepository) {
            // 自定义用户明细服务实现
            return new UserDetailsServiceImpl(userManager, userDetailsMetaRepository);
        }

        @Bean("defaultAuthenticationProvider")
        AuthenticationProvider defaultAuthenticationProvider(UserManager userManager,
                UserDetailsService userDetailsService, UserDetailsMetaRepository userDetailsMetaRepository,
                PasswordEncoder passwordEncoder, CaptchaHelper captchaHelper, SettingService settingService,
                TempStorage tempStorage) {
            // 自定义用户名/密码认证器
            return new DefaultAuthenticationProvider(userManager, userDetailsService, userDetailsMetaRepository,
                    passwordEncoder, captchaHelper, settingService, tempStorage);
        }

        @Bean
        DeviceAwareAuthenticationProcessingFilter usernameAndPasswordFilter(AuthenticationManager authenticationManager,
                UCenterConfigProperties uCenterConfigProperties) {
            // 注册“用户名/密码”登录过滤器
            val usernameAndPassword = uCenterConfigProperties.getEndpoint().getOrDefault("username-password", "login");
            val loginUrl = "/" + StringUtils.strip(StringUtils.defaultIfBlank(usernameAndPassword, "login"), "/");

            return new UsernameAndPasswordFilter(loginUrl, authenticationManager);
        }

        @Bean
        JwtTokenCustomizer ucenterTokenCustomizer(JwtTokenMapper jwtTokenMapper) {
            // 处理用户登录Token
            return new TokenCustomizer(jwtTokenMapper);
        }

    }

}
