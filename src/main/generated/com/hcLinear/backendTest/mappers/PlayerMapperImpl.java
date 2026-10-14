package com.hcLinear.backendTest.mappers;

import com.hcLinear.backendTest.dtos.PlayerCreateRequest;
import com.hcLinear.backendTest.dtos.PlayerResponse;
import com.hcLinear.backendTest.enums.PositionEnum;
import com.hcLinear.backendTest.models.PlayerModel;
import com.hcLinear.backendTest.models.TeamModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-14T19:45:17+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class PlayerMapperImpl implements PlayerMapper {

    @Override
    public PlayerModel toEntity(PlayerCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        PlayerModel.PlayerModelBuilder playerModel = PlayerModel.builder();

        playerModel.firstName( request.firstName() );
        playerModel.lastName( request.lastName() );
        playerModel.position( request.position() );
        playerModel.shirtNumber( request.shirtNumber() );
        playerModel.birthDate( request.birthDate() );
        playerModel.marketValue( request.marketValue() );

        return playerModel.build();
    }

    @Override
    public PlayerResponse toResponse(PlayerModel entity) {
        if ( entity == null ) {
            return null;
        }

        Long teamId = null;
        String teamName = null;
        Long id = null;
        String firstName = null;
        String lastName = null;
        PositionEnum position = null;
        Integer shirtNumber = null;
        LocalDate birthDate = null;
        BigDecimal marketValue = null;

        teamId = entityTeamId( entity );
        teamName = entityTeamName( entity );
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        position = entity.getPosition();
        shirtNumber = entity.getShirtNumber();
        birthDate = entity.getBirthDate();
        marketValue = entity.getMarketValue();

        String fullName = entity.getFirstName() + " " + entity.getLastName();

        PlayerResponse playerResponse = new PlayerResponse( id, firstName, lastName, fullName, position, shirtNumber, birthDate, marketValue, teamId, teamName );

        return playerResponse;
    }

    private Long entityTeamId(PlayerModel playerModel) {
        if ( playerModel == null ) {
            return null;
        }
        TeamModel team = playerModel.getTeam();
        if ( team == null ) {
            return null;
        }
        Long id = team.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityTeamName(PlayerModel playerModel) {
        if ( playerModel == null ) {
            return null;
        }
        TeamModel team = playerModel.getTeam();
        if ( team == null ) {
            return null;
        }
        String name = team.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
