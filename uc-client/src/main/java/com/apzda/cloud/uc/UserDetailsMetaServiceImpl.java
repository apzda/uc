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

import com.apzda.cloud.gsvc.core.GsvcContextHolder;
import com.apzda.cloud.gsvc.security.userdetails.UserDetailsMetaService;
import com.apzda.cloud.uc.proto.AccountService;
import com.apzda.cloud.uc.proto.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Slf4j
@RequiredArgsConstructor
public class UserDetailsMetaServiceImpl implements UserDetailsMetaService {
    private final AccountService accountService;
    private final ObjectMapper objectMapper;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(@NonNull UserDetails userDetails) {
        val username = userDetails.getUsername();
        val authorities = accountService.getAuthorities(Request.newBuilder().setUsername(username).build());

        if (authorities.getErrCode() == 0) {
            if (authorities.getAuthorityCount() > 0) {
                return authorities.getAuthorityList().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    @Override
    @NonNull
    public <R> Optional<R> getMetaData(@NonNull UserDetails userDetails, @NonNull String key, @NonNull Class<R> rClass) {
        val username = userDetails.getUsername();
        val metas = accountService.getMetas(Request.newBuilder().setUsername(username).setMetaName(key).build());
        if (metas.getErrCode() == 0 && metas.getMetaCount() > 0) {
            val meta = metas.getMeta(0);
            val value = meta.getValue();
            try {
                val type = meta.getType();
                return Optional.ofNullable(switch (type) {
                    case INTEGER -> rClass.cast(Integer.parseInt(value));
                    case LONG -> rClass.cast(Long.parseLong(value));
                    case FLOAT -> rClass.cast(Float.parseFloat(value));
                    case DOUBLE -> rClass.cast(Double.parseDouble(value));
                    case OBJECT -> objectMapper.readValue(value, rClass);
                    default -> rClass.cast(value);
                });
            } catch (JsonProcessingException e) {
                log.warn("[{}] Cannot parse meta value: {} - {}", GsvcContextHolder.getRequestId(), key, value);
            }
        }
        return Optional.empty();
    }

    @Override
    @NonNull
    public <R> Optional<R> getMultiMetaData(@NonNull UserDetails userDetails, @NonNull String key, @NonNull TypeReference<R> typeReference) {
        val username = userDetails.getUsername();
        val metas = accountService.getMetas(Request.newBuilder().setUsername(username).setMetaName(key).build());

        if (metas.getErrCode() == 0 && metas.getMetaCount() > 0) {
            val meta = metas.getMeta(0);
            val value = meta.getValue();
            try {
                val type = meta.getType();
                return Optional.ofNullable(switch (type) {
                    case OBJECT -> objectMapper.readValue(value, typeReference);
                    default -> objectMapper.readValue(String.format("[%s]", value), typeReference);
                });
            } catch (JsonProcessingException e) {
                log.warn("[{}] Cannot parse meta value: {} - {}", GsvcContextHolder.getRequestId(), key, meta.getValue());
            }
        }
        return Optional.empty();
    }
}
