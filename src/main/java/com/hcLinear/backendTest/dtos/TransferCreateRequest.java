package com.hcLinear.backendTest.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferCreateRequest(
        @NotNull(message = "A játékos megadása kötelező")
        Long playerId,

        Long sourceTeamId,

        @NotNull(message = "A célcsapat megadása kötelező")
        Long targetTeamId,

        @NotNull(message = "Az átigazolási összeg megadása kötelező")
        @DecimalMin(value = "0.0", inclusive = true, message = "Az átigazolási összeg nem lehet negatív")
        BigDecimal fee,

        LocalDate createdDate,

        @NotNull(message = "A lejárati dátum megadása kötelező")
        LocalDate expiryDate
) {
}
