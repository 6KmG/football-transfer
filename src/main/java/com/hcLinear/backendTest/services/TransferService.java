package com.hcLinear.backendTest.services;

import com.hcLinear.backendTest.dtos.TransferCreateRequest;
import com.hcLinear.backendTest.dtos.TransferResponse;
import com.hcLinear.backendTest.enums.TransferStatus;
import com.hcLinear.backendTest.exceptions.BusinessValidationException;
import com.hcLinear.backendTest.exceptions.ResourceNotFoundException;
import com.hcLinear.backendTest.mappers.TransferMapper;
import com.hcLinear.backendTest.models.PlayerModel;
import com.hcLinear.backendTest.models.TeamModel;
import com.hcLinear.backendTest.models.TransferRequestModel;
import com.hcLinear.backendTest.repositories.PlayerRepository;
import com.hcLinear.backendTest.repositories.TeamRepository;
import com.hcLinear.backendTest.repositories.TransferRequestRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private static final int MAX_SQUAD_SIZE = 25;

    private final TransferRequestRepository transferRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final TransferMapper transferMapper;

    @Transactional
    public TransferResponse create(TransferCreateRequest request) {
        PlayerModel player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Játékos nem található: " + request.playerId()));

        TeamModel targetTeam = teamRepository.findById(request.targetTeamId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Célcsapat nem található: " + request.targetTeamId()));

        TeamModel sourceTeam = null;
        if (request.sourceTeamId() != null) {
            sourceTeam = teamRepository.findById(request.sourceTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Forráscsapat nem található: " + request.sourceTeamId()));
        }

        LocalDate createdDate = request.createdDate() != null ? request.createdDate() : LocalDate.now();
        LocalDate expiryDate = request.expiryDate();

        validateDates(createdDate, expiryDate);
        validateFee(request.fee(), player, sourceTeam);
        validateTeams(player, sourceTeam, targetTeam);

        TransferRequestModel entity = transferMapper.toEntity(request);
        entity.setPlayer(player);
        entity.setSourceTeam(sourceTeam);
        entity.setTargetTeam(targetTeam);
        entity.setCreatedDate(createdDate);
        entity.setExpiryDate(expiryDate);
        entity.setStatus(TransferStatus.PENDING);

        TransferRequestModel saved = transferRepository.save(entity);
        return transferMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TransferResponse> list(Long playerId, Long sourceTeamId, Long targetTeamId,
                                      TransferStatus status) {
        return transferRepository.findFiltered(playerId, sourceTeamId, targetTeamId, status).stream()
                .map(transferMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransferResponse load(Long id) {
        TransferRequestModel transfer = transferRepository.findWithRelationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Átigazolási kérelem nem található: " + id));
        return transferMapper.toResponse(transfer);
    }

    @Transactional
    public TransferResponse accept(Long id) {
        TransferRequestModel transfer = transferRepository.findWithRelationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Átigazolási kérelem nem található: " + id));

        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new BusinessValidationException(
                    "Csak függőben lévő (PENDING) kérelem fogadható el, aktuális státusz: " + transfer.getStatus());
        }

        if (LocalDate.now().isAfter(transfer.getExpiryDate())) {
            throw new BusinessValidationException(
                    "A kérelem lejárt (" + transfer.getExpiryDate() + "), már nem fogadható el");
        }

        Long playerId = transfer.getPlayer().getId();
        PlayerModel player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Játékos nem található: " + playerId));

        Long targetTeamId = transfer.getTargetTeam().getId();
        TeamModel targetTeam = teamRepository.findById(targetTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("Célcsapat nem található: " + targetTeamId));

        TeamModel sourceTeam = null;
        if (transfer.getSourceTeam() != null) {
            Long sourceTeamId = transfer.getSourceTeam().getId();
            sourceTeam = teamRepository.findById(sourceTeamId)
                    .orElse(null);
        }

        validateTeams(player, sourceTeam, targetTeam);

        long targetSquadSize = playerRepository.countByTeamId(targetTeam.getId());
        boolean playerAlreadyInTarget = player.getTeam() != null
                && player.getTeam().getId().equals(targetTeam.getId());
        if (!playerAlreadyInTarget && targetSquadSize >= MAX_SQUAD_SIZE) {
            throw new BusinessValidationException(
                    "A célcsapat kerete betelt (max " + MAX_SQUAD_SIZE + " játékos)");
        }

        BigDecimal fee = transfer.getFee() != null ? transfer.getFee() : BigDecimal.ZERO;
        if (fee.signum() < 0) {
            throw new BusinessValidationException("Az átigazolási összeg nem lehet negatív");
        }
        if (targetTeam.getBudget() == null || targetTeam.getBudget().compareTo(fee) < 0) {
            throw new BusinessValidationException(
                    "A célcsapat költségkerete nem fedezi az átigazolási összeget");
        }

        targetTeam.setBudget(targetTeam.getBudget().subtract(fee));
        teamRepository.save(targetTeam);

        if (sourceTeam != null) {
            TeamModel freshSource = teamRepository.findById(sourceTeam.getId())
                    .orElse(null);
            if (freshSource != null) {
                BigDecimal sourceBudget = freshSource.getBudget() != null
                        ? freshSource.getBudget() : BigDecimal.ZERO;
                freshSource.setBudget(sourceBudget.add(fee));
                if (freshSource.getCaptain() != null
                        && freshSource.getCaptain().getId().equals(player.getId())) {
                    freshSource.setCaptain(null);
                }
                teamRepository.save(freshSource);
            }
        }

        player.setTeam(targetTeam);
        playerRepository.save(player);

        transfer.setStatus(TransferStatus.ACCEPTED);
        transferRepository.save(transfer);

        List<TransferRequestModel> others =
                transferRepository.findByPlayerIdAndStatus(playerId, TransferStatus.PENDING);
        for (TransferRequestModel other : others) {
            if (!other.getId().equals(transfer.getId())) {
                other.setStatus(TransferStatus.CLOSED);
            }
        }
        transferRepository.saveAll(others);

        return transferMapper.toResponse(transfer);
    }

    @Transactional
    public TransferResponse reject(Long id) {
        TransferRequestModel transfer = transferRepository.findWithRelationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Átigazolási kérelem nem található: " + id));

        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new BusinessValidationException(
                    "Csak függőben lévő (PENDING) kérelem utasítható el, aktuális státusz: " + transfer.getStatus());
        }

        transfer.setStatus(TransferStatus.REJECTED);
        TransferRequestModel saved = transferRepository.save(transfer);
        return transferMapper.toResponse(saved);
    }

    private void validateDates(LocalDate createdDate, LocalDate expiryDate) {
        if (expiryDate == null) {
            throw new BusinessValidationException("A lejárati dátum megadása kötelező");
        }
        if (expiryDate.isBefore(createdDate)) {
            throw new BusinessValidationException("A lejárati dátum nem lehet korábbi a keletkezés dátumánál");
        }
    }

    private void validateFee(BigDecimal fee, PlayerModel player, TeamModel sourceTeam) {
        if (fee == null) {
            throw new BusinessValidationException("Az átigazolási összeg megadása kötelező");
        }
        if (fee.signum() < 0) {
            throw new BusinessValidationException("Az átigazolási összeg nem lehet negatív");
        }
        boolean playerHasTeam = player.getTeam() != null;
        if (!playerHasTeam) {
            return;
        }
        if (fee.signum() == 0) {
            throw new BusinessValidationException(
                    "Csapattal rendelkező játékos esetén az átigazolási összeg nem lehet 0");
        }
        if (sourceTeam == null && fee.signum() == 0) {
            throw new BusinessValidationException(
                    "Forráscsapattal történő átigazolás esetén az összeg nem lehet 0");
        }
    }

    private void validateTeams(PlayerModel player, TeamModel sourceTeam, TeamModel targetTeam) {
        if (targetTeam == null) {
            throw new BusinessValidationException("A célcsapat megadása kötelező");
        }
        if (sourceTeam != null && sourceTeam.getId().equals(targetTeam.getId())) {
            throw new BusinessValidationException("A forrás- és célcsapat nem lehet azonos");
        }
        if (player.getTeam() != null && player.getTeam().getId().equals(targetTeam.getId())) {
            throw new BusinessValidationException("A játékos már a célcsapat tagja");
        }
        if (sourceTeam != null && player.getTeam() != null
                && !player.getTeam().getId().equals(sourceTeam.getId())) {
            throw new BusinessValidationException(
                    "A forráscsapat nem egyezik a játékos jelenlegi csapatával");
        }
        if (player.getTeam() == null && sourceTeam != null) {
            throw new BusinessValidationException(
                    "Csapat nélküli játékos esetén a forráscsapatnak üresnek kell lennie");
        }
    }
}
