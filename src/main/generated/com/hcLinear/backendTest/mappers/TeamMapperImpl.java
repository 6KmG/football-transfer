package com.hcLinear.backendTest.mappers;

import com.hcLinear.backendTest.dtos.TeamCreateRequest;
import com.hcLinear.backendTest.dtos.TeamResponse;
import com.hcLinear.backendTest.models.PlayerModel;
import com.hcLinear.backendTest.models.TeamModel;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-14T19:45:17+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class TeamMapperImpl implements TeamMapper {

    @Override
    public TeamModel toEntity(TeamCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        TeamModel.TeamModelBuilder teamModel = TeamModel.builder();

        teamModel.name( request.name() );
        teamModel.city( request.city() );
        teamModel.foundedYear( request.foundedYear() );
        teamModel.budget( request.budget() );

        return teamModel.build();
    }

    @Override
    public TeamResponse toResponse(TeamModel entity) {
        if ( entity == null ) {
            return null;
        }

        Long captainId = null;
        Long id = null;
        String name = null;
        String city = null;
        Integer foundedYear = null;
        BigDecimal budget = null;

        captainId = entityCaptainId( entity );
        id = entity.getId();
        name = entity.getName();
        city = entity.getCity();
        foundedYear = entity.getFoundedYear();
        budget = entity.getBudget();

        long playerCount = 0L;
        String captainName = entity.getCaptain() == null ? null : entity.getCaptain().getFirstName() + " " + entity.getCaptain().getLastName();

        TeamResponse teamResponse = new TeamResponse( id, name, city, foundedYear, budget, playerCount, captainId, captainName );

        return teamResponse;
    }

    private Long entityCaptainId(TeamModel teamModel) {
        if ( teamModel == null ) {
            return null;
        }
        PlayerModel captain = teamModel.getCaptain();
        if ( captain == null ) {
            return null;
        }
        Long id = captain.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
