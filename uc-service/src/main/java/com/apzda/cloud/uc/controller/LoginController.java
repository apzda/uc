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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Controller
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("${apzda.cloud.config.login-page:/login}")
    public String login(@RequestParam(value = "redirect_to", required = false) String redirectTo,
            @Value("${apzda.cloud.config.home-page:/}") String homePage) {
        log.warn("redirectTo {}, homePage: {}", redirectTo, homePage);

        return "redirect:" + StringUtils.defaultIfBlank(redirectTo, homePage);
    }

    @GetMapping("${apzda.cloud.config.logout-path:/logout}")
    public String logout(@Value("${apzda.cloud.config.home-page:/}") String homePage) {
        return "redirect:" + homePage;
    }

}
