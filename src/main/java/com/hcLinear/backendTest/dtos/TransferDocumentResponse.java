package com.hcLinear.backendTest.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransferDocumentResponse(
        Long id,
        String documentNumber,
        Long sequenceNumber,
        LocalDate transferDate,
        Long playerId,
        String playerName,
        Long sourceTeamId,
        String sourceTeamName,
        Long targetTeamId,
        String targetTeamName,
        BigDecimal fee,
        Long transferRequestId,
        LocalDateTime createdAt
) {
}
