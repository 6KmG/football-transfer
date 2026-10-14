package com.hcLinear.backendTest.mappers;

import com.hcLinear.backendTest.dtos.PlayerCreateRequest;
import com.hcLinear.backendTest.dtos.PlayerResponse;
import com.hcLinear.backendTest.models.PlayerModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    PlayerModel toEntity(PlayerCreateRequest request);

    @Mapping(target = "fullName", expression = "java(entity.getFirstName() + \" \" + entity.getLastName())")
    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamName", source = "team.name")
    PlayerResponse toResponse(PlayerModel entity);
}
