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
package com.apzda.cloud.uc.domain.entity;

import com.apzda.cloud.gsvc.domain.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/

@Getter
@Setter
@ToString
@Entity
@Table(name = "uc_user_oauth_session")
public class OauthSession extends AuditEntity {

    @NotNull
    @Column(name = "oauth_id", nullable = false)
    private Long oauthId;

    @NotNull
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Size(max = 256)
    @Column(name = "grant_code", length = 256)
    private String grantCode;

    @Size(max = 256)
    @Column(name = "access_token", length = 256)
    private String accessToken;

    @Size(max = 256)
    @Column(name = "refresh_token", length = 256)
    private String refreshToken;

    @NotNull
    @Column(name = "expiration", nullable = false)
    private Long expiration;

    @Size(max = 24)
    @NotNull
    @Column(name = "device", nullable = false, length = 24)
    private String device;

    @NotNull
    @Column(name = "simulator", nullable = false)
    private Boolean simulator = false;

    @Size(max = 256)
    @NotNull
    @Column(name = "ip", nullable = false, length = 256)
    private String ip;

    @Lob
    @Column(name = "extra")
    private String extra;

}
