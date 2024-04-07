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
package com.apzda.cloud.uc.server;

import com.apzda.cloud.uc.config.DruidConfig;
import com.apzda.cloud.uc.config.UCenterConfig;
import com.apzda.cloud.uc.proto.UcenterServiceGsvc;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PropertySource("classpath:apzda.uc.service.properties")
//@formatter:off
@Import({
    UCenterConfig.class,
    DruidConfig.class,
    UcenterServiceGsvc.class
})
@ComponentScan(basePackages = {
    "com.apzda.cloud.uc.controller",
    "com.apzda.cloud.uc.domain",
    "com.apzda.cloud.uc.facade",
    "com.apzda.cloud.uc.filter",
    "com.apzda.cloud.uc.service",
    "com.apzda.cloud.uc.security.listener"
})
//@formatter:on
@EnableJpaRepositories(basePackages = { "com.apzda.cloud.uc.domain.repository" })
@EntityScan("com.apzda.cloud.uc.domain.entity")
@Documented
public @interface EnableUCenterServer {

}
