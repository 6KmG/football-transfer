package com.hcLinear.backendTest.mappers;

import com.hcLinear.backendTest.dtos.TransferCreateRequest;
import com.hcLinear.backendTest.dtos.TransferResponse;
import com.hcLinear.backendTest.enums.TransferStatus;
import com.hcLinear.backendTest.models.PlayerModel;
import com.hcLinear.backendTest.models.TeamModel;
import com.hcLinear.backendTest.models.TransferRequestModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-14T20:19:53+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class TransferMapperImpl implements TransferMapper {

    @Override
    public TransferRequestModel toEntity(TransferCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        TransferRequestModel.TransferRequestModelBuilder transferRequestModel = TransferRequestModel.builder();

        transferRequestModel.fee( request.fee() );
        transferRequestModel.createdDate( request.createdDate() );
        transferRequestModel.expiryDate( request.expiryDate() );

        return transferRequestModel.build();
    }

    @Override
    public TransferResponse toResponse(TransferRequestModel entity) {
        if ( entity == null ) {
            return null;
        }

        Long playerId = null;
        Long sourceTeamId = null;
        String sourceTeamName = null;
        Long targetTeamId = null;
        String targetTeamName = null;
        Long id = null;
        BigDecimal fee = null;
        LocalDate createdDate = null;
        LocalDate expiryDate = null;
        TransferStatus status = null;

        playerId = entityPlayerId( entity );
        sourceTeamId = entitySourceTeamId( entity );
        sourceTeamName = entitySourceTeamName( entity );
        targetTeamId = entityTargetTeamId( entity );
        targetTeamName = entityTargetTeamName( entity );
        id = entity.getId();
        fee = entity.getFee();
        createdDate = entity.getCreatedDate();
        expiryDate = entity.getExpiryDate();
        status = entity.getStatus();

        String playerName = entity.getPlayer() == null ? null : entity.getPlayer().getFirstName() + " " + entity.getPlayer().getLastName();

        TransferResponse transferResponse = new TransferResponse( id, playerId, playerName, sourceTeamId, sourceTeamName, targetTeamId, targetTeamName, fee, createdDate, expiryDate, status );

        return transferResponse;
    }

    private Long entityPlayerId(TransferRequestModel transferRequestModel) {
        if ( transferRequestModel == null ) {
            return null;
        }
        PlayerModel player = transferRequestModel.getPlayer();
        if ( player == null ) {
            return null;
        }
        Long id = player.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entitySourceTeamId(TransferRequestModel transferRequestModel) {
        if ( transferRequestModel == null ) {
            return null;
        }
        TeamModel sourceTeam = transferRequestModel.getSourceTeam();
        if ( sourceTeam == null ) {
            return null;
        }
        Long id = sourceTeam.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entitySourceTeamName(TransferRequestModel transferRequestModel) {
        if ( transferRequestModel == null ) {
            return null;
        }
        TeamModel sourceTeam = transferRequestModel.getSourceTeam();
        if ( sourceTeam == null ) {
            return null;
        }
        String name = sourceTeam.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long entityTargetTeamId(TransferRequestModel transferRequestModel) {
        if ( transferRequestModel == null ) {
            return null;
        }
        TeamModel targetTeam = transferRequestModel.getTargetTeam();
        if ( targetTeam == null ) {
            return null;
        }
        Long id = targetTeam.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityTargetTeamName(TransferRequestModel transferRequestModel) {
        if ( transferRequestModel == null ) {
            return null;
        }
        TeamModel targetTeam = transferRequestModel.getTargetTeam();
        if ( targetTeam == null ) {
            return null;
        }
        String name = targetTeam.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
