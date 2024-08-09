package travelAgency.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CountryInputFormDTO(@NotBlank String name) {
}
