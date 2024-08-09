package travelAgency.api.dto;

import java.util.UUID;

public record DestinationCreateResponseDTO(UUID id, String name, CountryRepresentationDTO country) {
}
