package com.hcLinear.backendTest.dtos;

import com.hcLinear.backendTest.enums.TransferStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferResponse(
        Long id,
        Long playerId,
        String playerName,
        Long sourceTeamId,
        String sourceTeamName,
        Long targetTeamId,
        String targetTeamName,
        BigDecimal fee,
        LocalDate createdDate,
        LocalDate expiryDate,
        TransferStatus status
) {
}
