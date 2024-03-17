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
package com.apzda.cloud.uc.autoconfig;

import com.apzda.cloud.gsvc.context.TenantManager;
import com.apzda.cloud.gsvc.i18n.MessageSourceNameResolver;
import com.apzda.cloud.gsvc.security.config.GsvcSecurityAutoConfiguration;
import com.apzda.cloud.gsvc.security.userdetails.UserDetailsMetaRepository;
import com.apzda.cloud.gsvc.security.userdetails.UserDetailsMetaService;
import com.apzda.cloud.uc.ProxiedUserDetailsService;
import com.apzda.cloud.uc.UserDetailsMetaServiceImpl;
import com.apzda.cloud.uc.context.UCenterTenantManager;
import com.apzda.cloud.uc.proto.AccountService;
import com.apzda.cloud.uc.proto.AccountServiceGsvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@AutoConfiguration(before = GsvcSecurityAutoConfiguration.class)
@EnableMethodSecurity
@Import({AccountServiceGsvc.class})
@Slf4j
public class UCenterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    TenantManager<String> tenantManager() {
        return new UCenterTenantManager();
    }

    @Bean("uc.MessageSourceNameResolver")
    MessageSourceNameResolver messageSourceNameResolver() {
        return () -> "messages-uc";
    }

    @Bean
    @ConditionalOnMissingBean
    UserDetailsService userDetailsService(AccountService accountService, UserDetailsMetaRepository userDetailsMetaRepository) {
        return new ProxiedUserDetailsService(accountService, userDetailsMetaRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    UserDetailsMetaService userDetailsMetaService(AccountService accountService, ObjectMapper objectMapper) {
        return new UserDetailsMetaServiceImpl(accountService, objectMapper);
    }
}
