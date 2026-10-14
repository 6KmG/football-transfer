package com.hcLinear.backendTest.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record TeamCreateRequest(
        @NotBlank(message = "A csapatnév megadása kötelező")
        @Size(max = 150, message = "A csapatnév legfeljebb 150 karakter lehet")
        String name,

        @NotBlank(message = "A város megadása kötelező")
        @Size(max = 100, message = "A város legfeljebb 100 karakter lehet")
        String city,

        @NotNull(message = "Az alapítás éve megadása kötelező")
        @Min(value = 1850, message = "Az alapítás éve nem lehet 1850-nél korábbi")
        Integer foundedYear,

        @NotNull(message = "A költségkeret megadása kötelező")
        @DecimalMin(value = "0.0", inclusive = true, message = "A költségkeret nem lehet negatív")
        BigDecimal budget,

        Long captainId
) {
}
