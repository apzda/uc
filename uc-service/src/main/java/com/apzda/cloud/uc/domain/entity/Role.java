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

import com.apzda.cloud.gsvc.domain.TenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Getter
@Setter
@ToString
@Entity
@Table(name = "uc_role")
public class Role extends TenantEntity {

    @Size(max = 32)
    @NotNull
    @Column(name = "role", nullable = false, length = 32)
    private String role;

    @Size(max = 128)
    @NotNull
    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @NotNull
    @Column(name = "builtin", nullable = false)
    private Boolean builtin = false;

    @Size(max = 24)
    @NotNull
    @Column(name = "provider", nullable = false, length = 24)
    private String provider;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "uc_role_privilege", joinColumns = @JoinColumn(name = "role", referencedColumnName = "role"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    @ToString.Exclude
    private List<Privilege> privileges;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "uc_role_children", joinColumns = @JoinColumn(name = "role", referencedColumnName = "role"),
            inverseJoinColumns = @JoinColumn(name = "child", referencedColumnName = "role"))
    @ToString.Exclude
    private List<Role> children;

}
