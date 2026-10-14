package com.hcLinear.backendTest.dtos;

import com.hcLinear.backendTest.enums.PositionEnum;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PlayerResponse(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        PositionEnum position,
        Integer shirtNumber,
        LocalDate birthDate,
        BigDecimal marketValue,
        Long teamId,
        String teamName
) {
}
