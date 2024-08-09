package travelAgency.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TravelCreateResponseDTO(
        UUID id,
        String title,
        DestinationCreateResponseDTO destination,
        Double price,
        LocalDate startDate,
        LocalDate endDate
) {
}
