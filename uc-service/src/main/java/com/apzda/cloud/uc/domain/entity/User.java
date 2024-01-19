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
@Table(name = "uc_user")
public class User extends AuditEntity {

    @Size(max = 32)
    @NotNull
    @Column(name = "username", nullable = false, length = 32)
    private String username;

    @Size(max = 64)
    @Column(name = "nickname", length = 64)
    private String nickname;

    @Size(max = 128)
    @Column(name = "first_name", length = 128)
    private String firstName;

    @Size(max = 128)
    @Column(name = "last_name", length = 128)
    private String lastName;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Size(max = 10)
    @Column(name = "phone_prefix", length = 10)
    private String phonePrefix;

    @Size(max = 64)
    @Column(name = "email", length = 64)
    private String email;

    @Size(max = 512)
    @NotNull
    @Column(name = "passwd", nullable = false, length = 512)
    private String passwd;

    @Size(max = 1024)
    @Column(name = "avatar", length = 1024)
    private String avatar;

    @Lob
    @Column(name = "gender")
    private String gender;

    @Lob
    @Column(name = "status")
    private String status;

    @Size(max = 10)
    @Column(name = "realm", length = 10)
    private String realm;

    @NotNull
    @Column(name = "referrer_id", nullable = false)
    private Long referrerId;

    @Size(max = 256)
    @Column(name = "referrers", length = 256)
    private String referrers;

    @Column(name = "referrer_level", columnDefinition = "tinyint UNSIGNED not null")
    private Short referrerLevel;

    @Size(max = 32)
    @Column(name = "recommend_code", length = 32)
    private String recommendCode;

    @Size(max = 16)
    @Column(name = "channel", length = 16)
    private String channel;

    @Size(max = 256)
    @NotNull
    @Column(name = "ip", nullable = false, length = 256)
    private String ip;

    @Size(max = 24)
    @Column(name = "device", length = 24)
    private String device;

    @Size(max = 255)
    @Column(name = "remark")
    private String remark;

}
