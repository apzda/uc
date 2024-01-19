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
package com.apzda.cloud.uc;

import com.apzda.cloud.gsvc.security.userdetails.DefaultUserDetailsMeta;
import com.apzda.cloud.gsvc.security.userdetails.UserDetailsMetaRepository;
import com.apzda.cloud.uc.client.AccountService;
import com.apzda.cloud.uc.client.Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Slf4j
@RequiredArgsConstructor
public class ProxiedUserDetailsService implements UserDetailsService {

    private final AccountService accountService;

    private final UserDetailsMetaRepository userDetailsMetaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            val userInfo = accountService.getUserInfo(Request.newBuilder().setUsername(username).build());
            if (userInfo.getErrCode() == 0) {
                List<SimpleGrantedAuthority> authorities;
                if (userInfo.getAuthoritiesCount() > 0) {
                    authorities = userInfo.getAuthoritiesList().stream().map(SimpleGrantedAuthority::new).toList();
                }
                else {
                    authorities = Collections.emptyList();
                }
                user = new User(username, "", userInfo.getEnabled(), userInfo.getAccountNonExpired(),
                        userInfo.getCredentialsNonExpired(), userInfo.getAccountNonLocked(), authorities);
            }
            else {
                throw new IllegalStateException(userInfo.getErrMsg());
            }
        }
        catch (Exception e) {
            log.warn("Cannot load user info({}), use disabled anonymous instead - {}", username, e.getMessage());
            user = new User("anonymous", null, false, true, true, true, Collections.emptyList());
        }
        // only for load authorities. Cannot be used to identify a user!!!
        return new DefaultUserDetailsMeta(user, userDetailsMetaRepository);
    }

}
