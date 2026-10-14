package com.hcLinear.backendTest.services;

import com.hcLinear.backendTest.dtos.PlayerCreateRequest;
import com.hcLinear.backendTest.dtos.PlayerResponse;
import com.hcLinear.backendTest.exceptions.BusinessValidationException;
import com.hcLinear.backendTest.exceptions.ResourceNotFoundException;
import com.hcLinear.backendTest.mappers.PlayerMapper;
import com.hcLinear.backendTest.models.PlayerModel;
import com.hcLinear.backendTest.models.TeamModel;
import com.hcLinear.backendTest.repositories.PlayerRepository;
import com.hcLinear.backendTest.repositories.TeamRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private static final int MIN_AGE = 15;
    private static final int MIN_SHIRT_NUMBER = 1;
    private static final int MAX_SHIRT_NUMBER = 99;

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;

    @Transactional
    public PlayerResponse create(PlayerCreateRequest request) {
        validateShirtNumber(request.shirtNumber());
        validateAge(request.birthDate());
        validateMarketValue(request.marketValue());

        PlayerModel player = playerMapper.toEntity(request);

        if (request.teamId() != null) {
            TeamModel team = teamRepository.findById(request.teamId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Csapat nem található: " + request.teamId()));
            player.setTeam(team);
        }

        PlayerModel saved = playerRepository.save(player);
        return playerMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PlayerResponse> list() {
        return playerRepository.findAllWithTeam().stream()
                .map(playerMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlayerResponse load(Long id) {
        PlayerModel player = playerRepository.findWithTeamById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Játékos nem található: " + id));
        return playerMapper.toResponse(player);
    }

    @Transactional
    public void delete(Long id) {
        PlayerModel player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Játékos nem található: " + id));

        // Ha a játékos valamely csapat kapitánya, a referenciát nullázni kell (FK SET NULL).
        List<TeamModel> captainedTeams = teamRepository.findByCaptainId(id);
        for (TeamModel team : captainedTeams) {
            team.setCaptain(null);
        }
        teamRepository.saveAll(captainedTeams);

        playerRepository.delete(player);
    }

    private void validateShirtNumber(Integer shirtNumber) {
        if (shirtNumber != null
                && (shirtNumber < MIN_SHIRT_NUMBER || shirtNumber > MAX_SHIRT_NUMBER)) {
            throw new BusinessValidationException(
                    "A mezszám csak " + MIN_SHIRT_NUMBER + "-" + MAX_SHIRT_NUMBER + " tartományban lehet");
        }
    }

    private void validateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return;
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < MIN_AGE) {
            throw new BusinessValidationException(
                    "A játékosnak legalább " + MIN_AGE + " évesnek kell lennie");
        }
    }

    private void validateMarketValue(java.math.BigDecimal marketValue) {
        if (marketValue != null && marketValue.signum() < 0) {
            throw new BusinessValidationException("A piaci érték nem lehet negatív");
        }
    }
}
