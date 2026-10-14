package com.hcLinear.backendTest.services;

import com.hcLinear.backendTest.dtos.TeamCreateRequest;
import com.hcLinear.backendTest.dtos.TeamResponse;
import com.hcLinear.backendTest.exceptions.BusinessValidationException;
import com.hcLinear.backendTest.exceptions.DuplicateResourceException;
import com.hcLinear.backendTest.exceptions.ResourceNotFoundException;
import com.hcLinear.backendTest.mappers.TeamMapper;
import com.hcLinear.backendTest.models.PlayerModel;
import com.hcLinear.backendTest.models.TeamModel;
import com.hcLinear.backendTest.repositories.PlayerRepository;
import com.hcLinear.backendTest.repositories.TeamRepository;
import com.hcLinear.backendTest.repositories.TransferRequestRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private static final int MIN_FOUNDED_YEAR = 1850;

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TransferRequestRepository transferRequestRepository;
    private final TeamMapper teamMapper;

    @Transactional
    public TeamResponse create(TeamCreateRequest request) {
        validateFoundedYear(request.foundedYear());

        if (teamRepository.existsByName(request.name())) {
            throw new DuplicateResourceException(
                    "Ilyen nevű csapat már létezik: " + request.name());
        }

        TeamModel team = teamMapper.toEntity(request);

        if (request.captainId() != null) {
            PlayerModel captain = playerRepository.findById(request.captainId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Kapitánynak jelölt játékos nem található: " + request.captainId()));
            team.setCaptain(captain);
        }

        TeamModel saved = teamRepository.save(team);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> list() {
        return teamRepository.findAllWithCaptain().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamResponse load(Long id) {
        TeamModel team = teamRepository.findByIdWithCaptain(id)
                .orElseThrow(() -> new ResourceNotFoundException("Csapat nem található: " + id));
        return toResponse(team);
    }

    @Transactional
    public void delete(Long id) {
        TeamModel team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Csapat nem található: " + id));

        // Átigazolási kérelmek takarítása: a törölt csapathoz kötődő előzmények
        // nem maradhatnak árván (target kötelező, source opcionális).
        transferRequestRepository.deleteBySourceTeamIdOrTargetTeamId(id, id);

        // Játékosok nem törlődnek a csapattal: team_id SET NULL (FK is ezt teszi,
        // de JPA szinten is le kell választani a konzisztens állapothoz).
        List<PlayerModel> players = playerRepository.findByTeamId(id);
        for (PlayerModel player : players) {
            player.setTeam(null);
        }
        playerRepository.saveAll(players);

        teamRepository.delete(team);
    }

    private TeamResponse toResponse(TeamModel team) {
        TeamResponse base = teamMapper.toResponse(team);
        long playerCount = playerRepository.countByTeamId(team.getId());
        return new TeamResponse(
                base.id(),
                base.name(),
                base.city(),
                base.foundedYear(),
                base.budget(),
                playerCount,
                base.captainId(),
                base.captainName()
        );
    }

    private void validateFoundedYear(Integer foundedYear) {
        if (foundedYear == null) {
            return;
        }
        int currentYear = LocalDate.now().getYear();
        if (foundedYear < MIN_FOUNDED_YEAR) {
            throw new BusinessValidationException(
                    "Az alapítás éve nem lehet " + MIN_FOUNDED_YEAR + "-nél korábbi");
        }
        if (foundedYear > currentYear) {
            throw new BusinessValidationException(
                    "Az alapítás éve nem lehet jövőbeli (aktuális év: " + currentYear + ")");
        }
    }
}
