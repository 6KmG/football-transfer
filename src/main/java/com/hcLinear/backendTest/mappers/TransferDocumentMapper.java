package com.hcLinear.backendTest.mappers;

import com.hcLinear.backendTest.dtos.TransferDocumentResponse;
import com.hcLinear.backendTest.models.TransferDocumentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferDocumentMapper {

    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "playerName", expression = "java(entity.getPlayer() == null ? null : entity.getPlayer().getFirstName() + \" \" + entity.getPlayer().getLastName())")
    @Mapping(target = "sourceTeamId", source = "sourceTeam.id")
    @Mapping(target = "sourceTeamName", source = "sourceTeam.name")
    @Mapping(target = "targetTeamId", source = "targetTeam.id")
    @Mapping(target = "targetTeamName", source = "targetTeam.name")
    @Mapping(target = "transferRequestId", source = "transferRequest.id")
    TransferDocumentResponse toResponse(TransferDocumentModel entity);
}
