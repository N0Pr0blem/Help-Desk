package com.praktica.HelpDesk.mapper;

import com.praktica.HelpDesk.dto.user.UserOutputDto;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.mapper.base.Mappable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<UserEntity, UserOutputDto> {
}
