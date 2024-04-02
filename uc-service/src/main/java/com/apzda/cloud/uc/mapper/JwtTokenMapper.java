package com.apzda.cloud.uc.mapper;

import com.apzda.cloud.gsvc.security.token.JwtToken;
import com.apzda.cloud.uc.security.token.UserToken;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JwtTokenMapper {

    UserToken valueOf(JwtToken token);

}
