package com.hoang.vaultly.modules.fund.mapper;

import com.hoang.vaultly.modules.fund.dto.response.FundMemberResponse;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.fund.entity.FundMemberRole;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FundMemberMapper {
    @Mapping(target = "fundMemberId", source = "id")
    @Mapping(target = "fundId", source = "fund.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "displayName", source = "user.displayName")
    @Mapping(target = "roles", expression = "java(extractRoles(member))")
    FundMemberResponse toFundMemberResponse(FundMember member);

    default Set<FundRole> extractRoles(FundMember member) {
        if(member == null || member.getRoles() == null)
            return Collections.emptySet();
        return member.getRoles().stream().map(FundMemberRole::getRole).collect(Collectors.toSet());
    }
}
