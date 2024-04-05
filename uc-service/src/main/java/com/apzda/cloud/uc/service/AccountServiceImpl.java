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
package com.apzda.cloud.uc.service;

import com.apzda.cloud.uc.domain.entity.UserMeta;
import com.apzda.cloud.uc.domain.mapper.MetaTypeMapper;
import com.apzda.cloud.uc.domain.service.UserManager;
import com.apzda.cloud.uc.domain.vo.UserStatus;
import com.apzda.cloud.uc.proto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final UserManager userManager;
    private final MetaTypeMapper metaTypeMapper;

    @Override
    @Transactional(timeout = 3, readOnly = true)
    public UserInfo getUserInfo(Request request) {
        val builder = UserInfo.newBuilder();
        try {
            builder.setErrCode(0);
            val username = request.getUsername();
            val user = userManager.getUserByUsername(username);
            builder.setUid(username);
            builder.setUsername(username);
            val status = user.getStatus();
            builder.setEnabled(status == UserStatus.ACTIVATED || status == UserStatus.PENDING);
            builder.setAccountNonLocked(status != UserStatus.LOCKED);
            builder.setAccountNonExpired(status != UserStatus.EXPIRED);
            builder.setCredentialsNonExpired(userManager.isCredentialsExpired(user.getId()));
            if (request.hasAll() && request.getAll()) {
                val metas = getMetas(request);
                builder.addAllMeta(metas.getMetaList());
                val org = getOrganizations(request);
                builder.addAllOrg(org.getOrgList());
                val authorities = getAuthorities(request);
                builder.addAllAuthority(authorities.getAuthorityList());
            }
        } catch (UsernameNotFoundException e) {
            builder.setErrCode(404);
            builder.setErrMsg("User not found");
        }
        return builder.build();
    }

    @Override
    public UserMetaResp getMetas(Request request) {
        val builder = UserMetaResp.newBuilder().setErrCode(0);
        try {
            val username = request.getUsername();
            val user = userManager.getUserByUsername(username);
            val metaName = request.getMetaName();
            List<UserMeta> metas;
            if (StringUtils.isBlank(metaName)) {
                metas = userManager.getUserMetas(user.getId());
            } else {
                metas = userManager.getUserMetas(user.getId(), metaName);
            }

            val collect = metas.stream().map(meta -> {
                val b = com.apzda.cloud.uc.proto.UserMeta.newBuilder();
                b.setName(meta.getName());
                b.setType(metaTypeMapper.fromMetaType(meta.getType()));
                b.setValue(meta.getValue());
                return b.build();
            }).toList();
            builder.addAllMeta(collect);
        } catch (UsernameNotFoundException e) {
            builder.setErrCode(404);
            builder.setErrMsg(e.getMessage());
        }
        return builder.build();
    }

    @Override
    public AuthorityResp getAuthorities(Request request) {
        val builder = AuthorityResp.newBuilder().setErrCode(0);

        return builder.build();
    }

    @Override
    public OrganizationResp getOrganizations(Request request) {
        val builder = OrganizationResp.newBuilder().setErrCode(0);

        return builder.build();
    }
}
