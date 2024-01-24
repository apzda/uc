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
import com.apzda.cloud.uc.client.MetaValueType;
import com.apzda.cloud.uc.domain.vo.MetaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Getter
@Setter
@ToString
@Entity
@Table(name = "uc_user_meta")
public class UserMeta extends AuditEntity {

    public static final String CREDENTIALS_EXPIRED_AT = "credentials_expired_at";

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MetaType type;

    @NotNull
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Lob
    @Column(name = "value", columnDefinition = "LONGTEXT")
    private String value;

    @Column(name = "remark")
    private String remark;

    public com.apzda.cloud.uc.client.UserMeta convert() {
        val builder = com.apzda.cloud.uc.client.UserMeta.newBuilder();
        builder.setName(name);
        builder.setValue(value);
        switch (type) {
            case D -> builder.setType(MetaValueType.DOUBLE);
            case F -> builder.setType(MetaValueType.FLOAT);
            case I -> builder.setType(MetaValueType.INTEGER);
            case L -> builder.setType(MetaValueType.LONG);
            default -> builder.setType(MetaValueType.STRING);
        }
        return builder.build();
    }

}
