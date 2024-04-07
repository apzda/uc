/*
 * Copyright (C) 2023-2024 Fengz Ning (windywany@gmail.com)
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
package com.apzda.cloud.uc.domain.service.impl;

import cn.hutool.core.date.DateUtil;
import com.apzda.cloud.gsvc.security.authentication.AuthenticationDetails;
import com.apzda.cloud.gsvc.security.token.JwtAuthenticationToken;
import com.apzda.cloud.gsvc.security.userdetails.UserDetailsMeta;
import com.apzda.cloud.uc.domain.entity.Oauth;
import com.apzda.cloud.uc.domain.entity.OauthSession;
import com.apzda.cloud.uc.domain.entity.User;
import com.apzda.cloud.uc.domain.entity.UserMeta;
import com.apzda.cloud.uc.domain.repository.OauthRepository;
import com.apzda.cloud.uc.domain.repository.OauthSessionRepository;
import com.apzda.cloud.uc.domain.repository.UserMetaRepository;
import com.apzda.cloud.uc.domain.repository.UserRepository;
import com.apzda.cloud.uc.domain.service.UserManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.apzda.cloud.uc.domain.entity.OauthSession.SIMULATOR;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagerImpl implements UserManager {

    private final UserRepository userRepository;

    private final OauthRepository oauthRepository;

    private final OauthSessionRepository oauthSessionRepository;

    private final UserMetaRepository userMetaRepository;

    @Override
    public User getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("username is blank");
        }
        val oauth = oauthRepository.findByOpenIdAndProvider(username, Oauth.SIMPLE);
        if (oauth.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        val user = oauth.get().getUser();

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }

    @Override
    public boolean isCredentialsExpired(@NonNull User user) {
        val credentialsExpiredAt = userMetaRepository.getByUserAndName(user, UserMeta.CREDENTIALS_EXPIRED_AT);
        if (credentialsExpiredAt.isEmpty()) {
            return false;
        }
        val userMeta = credentialsExpiredAt.get();
        val value = userMeta.getValue();
        try {
            val expiredAt = Long.parseLong(value);
            if (expiredAt < System.currentTimeMillis()) {
                return true;
            }
        }
        catch (Exception e) {
            log.warn("Cannot parse user({})'s credentialsExpiredAt({}) to long: {}", user.getId(), value,
                    e.getMessage());
            return true;
        }

        return false;
    }

    public List<UserMeta> getUserMetas(@NonNull User user, String name) {
        return userMetaRepository.findAllByUserAndName(user, name);
    }

    @Override
    @Transactional
    @Modifying
    public void onAuthenticated(AbstractAuthenticationToken token, Oauth oauth) {
        // Oauth Data
        oauth = oauthRepository.findByOpenIdAndProvider(oauth.getOpenId(), oauth.getProvider()).orElse(null);
        if (oauth == null) {
            throw new UsernameNotFoundException(String.format("%s is not found", token.getName()));
        }
        // Last Login Info
        oauth.setLastLoginTime(DateUtil.current());
        oauth.setLastIp("127.0.0.1");
        oauth.setLastDevice("UNKNOWN");
        if (token.getDetails() instanceof AuthenticationDetails details) {
            oauth.setLastDevice(details.getDevice());
            oauth.setLastIp(details.getRemoteAddress());
        }
    }

    @Override
    @Transactional
    @Modifying
    public void createOauthSession(JwtAuthenticationToken token) {
        // Record Login Session
        val userDetails = (UserDetailsMeta) token.getPrincipal();
        val oauth = oauthRepository.findByOpenIdAndProvider(userDetails.getUsername(), userDetails.getProvider())
            .orElse(null);
        if (oauth == null) {
            throw new UsernameNotFoundException(String.format("%s is not found", token.getName()));
        }
        val session = newOauthSession(token, oauth);
        oauthSessionRepository.save(session);
        Objects.requireNonNull(session.getId());
        log.trace("Session recorded: {}", oauth.getOpenId());
    }

    @NonNull
    public static OauthSession newOauthSession(AbstractAuthenticationToken token, Oauth oauth) {
        val session = new OauthSession();
        session.setOauth(oauth);
        session.setUser(oauth.getUser());
        session.setDevice(oauth.getDevice());
        session.setIp(oauth.getIp());
        session.setSimulator(SIMULATOR.equals(oauth.getDevice()));
        if (token instanceof JwtAuthenticationToken) {
            session.setAccessToken(((JwtAuthenticationToken) token).getJwtToken().getAccessToken());
        }
        session.setExpiration(0L);
        return session;
    }

}
