package com.hcLinear.backendTest.services;

import com.hcLinear.backendTest.dtos.TransferDocumentResponse;
import com.hcLinear.backendTest.exceptions.ResourceNotFoundException;
import com.hcLinear.backendTest.mappers.TransferDocumentMapper;
import com.hcLinear.backendTest.models.PlayerModel;
import com.hcLinear.backendTest.models.TeamModel;
import com.hcLinear.backendTest.models.TransferDocumentModel;
import com.hcLinear.backendTest.models.TransferRequestModel;
import com.hcLinear.backendTest.repositories.TransferDocumentRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferDocumentService {

    private final TransferDocumentRepository documentRepository;
    private final TransferDocumentMapper documentMapper;
    private final TransferDocumentNumberGenerator numberGenerator;

    @Transactional
    public TransferDocumentResponse createForAcceptedTransfer(TransferRequestModel transfer,
                                                             PlayerModel player,
                                                             TeamModel sourceTeam,
                                                             TeamModel targetTeam,
                                                             BigDecimal fee,
                                                             LocalDate transferDate) {
        return documentMapper.toResponse(
                createEntityForAcceptedTransfer(transfer, player, sourceTeam, targetTeam, fee, transferDate));
    }

    @Transactional
    public TransferDocumentModel createEntityForAcceptedTransfer(TransferRequestModel transfer,
                                                                PlayerModel player,
                                                                TeamModel sourceTeam,
                                                                TeamModel targetTeam,
                                                                BigDecimal fee,
                                                                LocalDate transferDate) {
        return documentRepository.findByTransferRequestId(transfer.getId())
                .orElseGet(() -> {
                    Long sequenceNumber = documentRepository.nextSequenceValue();
                    String documentNumber = numberGenerator.generate(sequenceNumber);
                    TransferDocumentModel document = TransferDocumentModel.builder()
                            .documentNumber(documentNumber)
                            .sequenceNumber(sequenceNumber)
                            .transferDate(transferDate != null ? transferDate : LocalDate.now())
                            .player(player)
                            .sourceTeam(sourceTeam)
                            .targetTeam(targetTeam)
                            .fee(fee != null ? fee : BigDecimal.ZERO)
                            .transferRequest(transfer)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return documentRepository.save(document);
                });
    }

    @Transactional(readOnly = true)
    public List<TransferDocumentResponse> list(Long playerId, Long sourceTeamId, Long targetTeamId,
                                               String documentNumber) {
        return documentRepository.findFiltered(playerId, sourceTeamId, targetTeamId, documentNumber).stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransferDocumentResponse load(Long id) {
        TransferDocumentModel document = documentRepository.findWithRelationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Átigazolási dokumentum nem található: " + id));
        return documentMapper.toResponse(document);
    }

    @Transactional(readOnly = true)
    public TransferDocumentResponse loadByDocumentNumber(String documentNumber) {
        TransferDocumentModel document = documentRepository.findWithRelationsByDocumentNumber(documentNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Átigazolási dokumentum nem található: " + documentNumber));
        return documentMapper.toResponse(document);
    }
}
