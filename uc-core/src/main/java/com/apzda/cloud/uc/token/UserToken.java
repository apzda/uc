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
package com.apzda.cloud.uc.token;

import com.apzda.cloud.gsvc.security.token.JwtToken;
import com.apzda.cloud.uc.vo.Organization;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserToken implements JwtToken, Serializable {

    @Serial
    private static final long serialVersionUID = -2763131228048354173L;

    private String uid;

    private String name;

    private String displayName;

    private String avatar;

    private String phone;

    private String email;

    private String status;

    private String accessToken;

    private String refreshToken;

    private String orgId;

    private String deptId;

    private String mfa;

    private List<String> authorities;

    private List<Organization> orgs;

}
