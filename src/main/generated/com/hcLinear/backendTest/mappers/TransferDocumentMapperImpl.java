package com.hcLinear.backendTest.mappers;

import com.hcLinear.backendTest.dtos.TransferDocumentResponse;
import com.hcLinear.backendTest.models.PlayerModel;
import com.hcLinear.backendTest.models.TeamModel;
import com.hcLinear.backendTest.models.TransferDocumentModel;
import com.hcLinear.backendTest.models.TransferRequestModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-14T20:39:16+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class TransferDocumentMapperImpl implements TransferDocumentMapper {

    @Override
    public TransferDocumentResponse toResponse(TransferDocumentModel entity) {
        if ( entity == null ) {
            return null;
        }

        Long playerId = null;
        Long sourceTeamId = null;
        String sourceTeamName = null;
        Long targetTeamId = null;
        String targetTeamName = null;
        Long transferRequestId = null;
        Long id = null;
        String documentNumber = null;
        Long sequenceNumber = null;
        LocalDate transferDate = null;
        BigDecimal fee = null;
        LocalDateTime createdAt = null;

        playerId = entityPlayerId( entity );
        sourceTeamId = entitySourceTeamId( entity );
        sourceTeamName = entitySourceTeamName( entity );
        targetTeamId = entityTargetTeamId( entity );
        targetTeamName = entityTargetTeamName( entity );
        transferRequestId = entityTransferRequestId( entity );
        id = entity.getId();
        documentNumber = entity.getDocumentNumber();
        sequenceNumber = entity.getSequenceNumber();
        transferDate = entity.getTransferDate();
        fee = entity.getFee();
        createdAt = entity.getCreatedAt();

        String playerName = entity.getPlayer() == null ? null : entity.getPlayer().getFirstName() + " " + entity.getPlayer().getLastName();

        TransferDocumentResponse transferDocumentResponse = new TransferDocumentResponse( id, documentNumber, sequenceNumber, transferDate, playerId, playerName, sourceTeamId, sourceTeamName, targetTeamId, targetTeamName, fee, transferRequestId, createdAt );

        return transferDocumentResponse;
    }

    private Long entityPlayerId(TransferDocumentModel transferDocumentModel) {
        if ( transferDocumentModel == null ) {
            return null;
        }
        PlayerModel player = transferDocumentModel.getPlayer();
        if ( player == null ) {
            return null;
        }
        Long id = player.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entitySourceTeamId(TransferDocumentModel transferDocumentModel) {
        if ( transferDocumentModel == null ) {
            return null;
        }
        TeamModel sourceTeam = transferDocumentModel.getSourceTeam();
        if ( sourceTeam == null ) {
            return null;
        }
        Long id = sourceTeam.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entitySourceTeamName(TransferDocumentModel transferDocumentModel) {
        if ( transferDocumentModel == null ) {
            return null;
        }
        TeamModel sourceTeam = transferDocumentModel.getSourceTeam();
        if ( sourceTeam == null ) {
            return null;
        }
        String name = sourceTeam.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long entityTargetTeamId(TransferDocumentModel transferDocumentModel) {
        if ( transferDocumentModel == null ) {
            return null;
        }
        TeamModel targetTeam = transferDocumentModel.getTargetTeam();
        if ( targetTeam == null ) {
            return null;
        }
        Long id = targetTeam.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityTargetTeamName(TransferDocumentModel transferDocumentModel) {
        if ( transferDocumentModel == null ) {
            return null;
        }
        TeamModel targetTeam = transferDocumentModel.getTargetTeam();
        if ( targetTeam == null ) {
            return null;
        }
        String name = targetTeam.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long entityTransferRequestId(TransferDocumentModel transferDocumentModel) {
        if ( transferDocumentModel == null ) {
            return null;
        }
        TransferRequestModel transferRequest = transferDocumentModel.getTransferRequest();
        if ( transferRequest == null ) {
            return null;
        }
        Long id = transferRequest.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
