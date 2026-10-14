package com.hcLinear.backendTest.mappers;

import com.hcLinear.backendTest.dtos.TeamCreateRequest;
import com.hcLinear.backendTest.dtos.TeamResponse;
import com.hcLinear.backendTest.models.TeamModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "captain", ignore = true)
    TeamModel toEntity(TeamCreateRequest request);

    @Mapping(target = "playerCount", ignore = true)
    @Mapping(target = "captainId", source = "captain.id")
    @Mapping(target = "captainName", expression = "java(entity.getCaptain() == null ? null : entity.getCaptain().getFirstName() + \" \" + entity.getCaptain().getLastName())")
    TeamResponse toResponse(TeamModel entity);
}
