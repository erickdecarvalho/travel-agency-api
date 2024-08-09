package travelAgency.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TravelInputFormDTO(
        @NotBlank(message = "campo título não pode ser nulo ou vazio")
        String title,
        @NotNull(message = "campo destinationId não pode ser nulo")
        UUID destinationId,
        @NotNull(message = "campo price não pode ser nulo")
        Double price,
        @NotNull(message = "campo startDate não pode ser nulo")
        LocalDate startDate,
        @NotNull (message = "campo endDate não pode ser nulo")
        LocalDate endDate
) {
    public TravelInputFormDTO {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Data de início deve estar antes da data de término.");
        }
    }
}
