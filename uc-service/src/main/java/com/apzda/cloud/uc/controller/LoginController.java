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
package com.apzda.cloud.uc.controller;

import com.apzda.cloud.gsvc.security.token.JwtAuthenticationToken;
import com.apzda.cloud.gsvc.security.token.TokenManager;
import com.apzda.cloud.uc.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Controller
@RequestMapping("/login")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final TokenManager tokenManager;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    @Transactional
    @Modifying
    public ModelAndView login(@RequestParam(value = "redirect_to", required = false) String redirectTo) {
        log.warn("redirectTo {}", redirectTo);
        sign();
        return null;
    }

    private void sign() {
        val username = "admin";
        val password = "123456";
        val authRequest = JwtAuthenticationToken.unauthenticated(username, password);
        val authenticate = authenticationManager.authenticate(authRequest);
        if (authenticate != null && authenticate.isAuthenticated()) {
            val jwtToken = tokenManager.createJwtToken(authenticate);

            if (authenticate instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                jwtAuthenticationToken.login(jwtToken);
            }

            val context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
            context.setAuthentication(authenticate);
            SecurityContextHolder.setContext(context);
        }
    }

}
