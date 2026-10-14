package com.hcLinear.backendTest.dtos;

import java.math.BigDecimal;

public record TeamResponse(
        Long id,
        String name,
        String city,
        Integer foundedYear,
        BigDecimal budget,
        long playerCount,
        Long captainId,
        String captainName
) {
}
