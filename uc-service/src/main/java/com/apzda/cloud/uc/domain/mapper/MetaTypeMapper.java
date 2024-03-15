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
package com.apzda.cloud.uc.domain.mapper;

import com.apzda.cloud.uc.client.MetaValueType;
import com.apzda.cloud.uc.domain.vo.MetaType;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Mapper(componentModel = "spring")
public interface MetaTypeMapper {
    @ValueMappings({
        @ValueMapping(target = "S", source = "STRING"),
        @ValueMapping(target = "I", source = "INTEGER"),
        @ValueMapping(target = "L", source = "LONG"),
        @ValueMapping(target = "D", source = "DOUBLE"),
        @ValueMapping(target = "F", source = "FLOAT"),
        @ValueMapping(target = "O", source = "OBJECT"),
        @ValueMapping(target = "S", source = "UNRECOGNIZED")
    })
    MetaType fromMetaValueType(MetaValueType metaValueType);

    @InheritInverseConfiguration
    MetaValueType fromMetaType(MetaType metaType);
}
