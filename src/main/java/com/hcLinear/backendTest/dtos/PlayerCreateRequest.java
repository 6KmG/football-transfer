package com.hcLinear.backendTest.dtos;

import com.hcLinear.backendTest.enums.PositionEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PlayerCreateRequest(
        @NotBlank(message = "A keresztnév megadása kötelező")
        @Size(max = 50, message = "A keresztnév legfeljebb 50 karakter lehet")
        String firstName,

        @NotBlank(message = "A vezetéknév megadása kötelező")
        @Size(max = 50, message = "A vezetéknév legfeljebb 50 karakter lehet")
        String lastName,

        @NotNull(message = "A poszt megadása kötelező (GK, DF, MF, FW)")
        PositionEnum position,

        @NotNull(message = "A mezszám megadása kötelező")
        @Min(value = 1, message = "A mezszám legalább 1 lehet")
        @Max(value = 99, message = "A mezszám legfeljebb 99 lehet")
        Integer shirtNumber,

        @NotNull(message = "A születési dátum megadása kötelező")
        @Past(message = "A születési dátumnak múltbelinek kell lennie")
        LocalDate birthDate,

        @NotNull(message = "A piaci érték megadása kötelező")
        @DecimalMin(value = "0.0", inclusive = true, message = "A piaci érték nem lehet negatív")
        BigDecimal marketValue,

        Long teamId
) {
}
