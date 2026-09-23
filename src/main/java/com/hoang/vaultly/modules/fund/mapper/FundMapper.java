package com.hoang.vaultly.modules.fund.mapper;

import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.entity.Fund;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FundMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "members", ignore = true)
    Fund toFund(FundCreationRequest request);

    @Mapping(target = "fundId", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy.userId")
    FundCreationResponse toFundCreationResponse(Fund fund);


}
