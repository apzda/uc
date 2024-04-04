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

import com.apzda.cloud.gsvc.security.token.JwtAuthenticationToken;
import com.apzda.cloud.uc.domain.entity.Oauth;
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
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // todo
        val userWrapper = userRepository.getByUsername(username);

        if (userWrapper.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return userWrapper.get();
    }

    @Override
    public boolean isCredentialsExpired(Long uid) {
        val credentialsExpiredAt = userMetaRepository.getByUidAndName(uid, UserMeta.CREDENTIALS_EXPIRED_AT);
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
            log.warn("Cannot parse user({})'s credentialsExpiredAt({}) to long: {}", uid, value, e.getMessage());
            return true;
        }

        return false;
    }

    @Override
    public List<UserMeta> getUserMetas(@NonNull Long uid) {
        return userMetaRepository.findAllByUid(uid);
    }

    public List<UserMeta> getUserMetas(@NonNull Long uid, String name) {
        return userMetaRepository.findAllByUidAndName(uid, name);
    }

    @Override
    public void onAuthenticated(JwtAuthenticationToken token, Oauth oauth) {
        // 查看原信息
        val origOauth = oauthRepository.findByOpenIdAndProvider(oauth.getOpenId(), oauth.getProvider());

    }

}
