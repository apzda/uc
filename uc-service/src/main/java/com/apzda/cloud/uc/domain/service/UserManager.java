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
package com.apzda.cloud.uc.domain.service;

import com.apzda.cloud.gsvc.security.token.JwtAuthenticationToken;
import com.apzda.cloud.uc.domain.entity.Oauth;
import com.apzda.cloud.uc.domain.entity.User;
import com.apzda.cloud.uc.domain.entity.UserMeta;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
public interface UserManager {

    User getUserByUsername(String username);

    boolean isCredentialsExpired(@NonNull User user);

    List<UserMeta> getUserMetas(@NonNull User user, String name);

    /**
     * 登录成功后续流程.
     * @param token 认证的令牌
     * @param oauth 三方认证信息
     */
    void onAuthenticated(AbstractAuthenticationToken token, Oauth oauth);

    void createOauthSession(JwtAuthenticationToken token);

}
