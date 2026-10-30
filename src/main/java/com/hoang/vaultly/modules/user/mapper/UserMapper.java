package com.hoang.vaultly.modules.user.mapper;

import com.hoang.vaultly.modules.user.dto.response.UserResponse;
import com.hoang.vaultly.modules.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
