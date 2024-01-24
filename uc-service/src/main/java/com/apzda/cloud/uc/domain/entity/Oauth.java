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
@Table(name = "uc_user_oauth")
public class Oauth extends AuditEntity {

    public static final String SIMPLE = "simple";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    @NotNull
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Size(max = 24)
    @NotNull
    @Column(name = "provider", nullable = false, length = 24)
    private String provider;

    @Size(max = 256)
    @NotNull
    @Column(name = "open_id", nullable = false, length = 256)
    private String openId;

    @Size(max = 256)
    @NotNull
    @Column(name = "union_id", nullable = false, length = 256)
    private String unionId;

    @NotNull
    @Column(name = "login_time", nullable = false)
    private Long loginTime;

    @Size(max = 24)
    @NotNull
    @Column(name = "device", nullable = false, length = 24)
    private String device;

    @Size(max = 256)
    @NotNull
    @Column(name = "ip", nullable = false, length = 256)
    private String ip;

    @NotNull
    @Column(name = "last_login_time", nullable = false)
    private Long lastLoginTime;

    @Size(max = 24)
    @NotNull
    @Column(name = "last_device", nullable = false, length = 24)
    private String lastDevice;

    @Size(max = 256)
    @NotNull
    @Column(name = "last_ip", nullable = false, length = 256)
    private String lastIp;

    @Size(max = 255)
    @Column(name = "remark")
    private String remark;

}
