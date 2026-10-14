package com.hcLinear.backendTest.mappers;

import com.hcLinear.backendTest.dtos.TransferCreateRequest;
import com.hcLinear.backendTest.dtos.TransferResponse;
import com.hcLinear.backendTest.models.TransferRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "sourceTeam", ignore = true)
    @Mapping(target = "targetTeam", ignore = true)
    @Mapping(target = "status", ignore = true)
    TransferRequestModel toEntity(TransferCreateRequest request);

    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "playerName", expression = "java(entity.getPlayer() == null ? null : entity.getPlayer().getFirstName() + \" \" + entity.getPlayer().getLastName())")
    @Mapping(target = "sourceTeamId", source = "sourceTeam.id")
    @Mapping(target = "sourceTeamName", source = "sourceTeam.name")
    @Mapping(target = "targetTeamId", source = "targetTeam.id")
    @Mapping(target = "targetTeamName", source = "targetTeam.name")
    TransferResponse toResponse(TransferRequestModel entity);
}
