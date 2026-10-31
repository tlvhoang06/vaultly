package com.hoang.vaultly.modules.fund.mapper;

import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.dto.response.FundResponse;
import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.fund.entity.FundMemberRole;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FundMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "members", ignore = true)
    Fund toFund(FundCreationRequest request);

    @Mapping(target = "createdBy", source = "createdBy.id")
    FundCreationResponse toFundCreationResponse(Fund fund);

    @Mapping(target = "id", source = "fund.id")
    @Mapping(target = "name", source = "fund.name")
    @Mapping(target = "description", source = "fund.description")
    @Mapping(target = "status", source = "fund.status")
    @Mapping(target = "currency", source = "fund.currency")
    @Mapping(target = "createdBy", source = "fund.createdBy.id")
    @Mapping(target = "createdAt", source = "fund.createdAt")
    @Mapping(target = "myRoles", expression = "java(extractRoles(member))")
    FundResponse toFundResponse(FundMember member);

    default Set<FundRole> extractRoles(FundMember member) {
        if (member == null || member.getRoles() == null) {
            return Collections.emptySet();
        }
        return member.getRoles().stream()
                .map(FundMemberRole::getRole)
                .collect(Collectors.toSet());
    }
}
