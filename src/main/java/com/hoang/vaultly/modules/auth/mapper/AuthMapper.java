package com.hoang.vaultly.modules.auth.mapper;

import com.hoang.vaultly.modules.auth.dto.response.RegisterResponse;
import com.hoang.vaultly.modules.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "message", constant = "Register success")
    RegisterResponse toRegisterResponse(User user);
}
