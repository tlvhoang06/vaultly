package com.hoang.vaultly.modules.user.mapper;

import com.hoang.vaultly.modules.auth.dto.response.RegisterResponse;
import com.hoang.vaultly.modules.user.dto.response.UserResponse;
import com.hoang.vaultly.modules.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    @Mapping(target = "message", constant = "Register success")
    RegisterResponse toRegisterResponse(User user);

}
