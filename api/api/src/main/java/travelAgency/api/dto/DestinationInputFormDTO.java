package travelAgency.api.dto;

import java.util.UUID;

public record DestinationInputFormDTO(String name, UUID countryId) {
}
