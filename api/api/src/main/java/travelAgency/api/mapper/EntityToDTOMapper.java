package travelAgency.api.mapper;

import travelAgency.api.dto.CountryRepresentationDTO;
import travelAgency.api.dto.DestinationCreateResponseDTO;
import travelAgency.api.model.Country;
import travelAgency.api.model.Destination;

import java.util.Optional;

public class EntityToDTOMapper {

    public static CountryRepresentationDTO toCountryDTO(Country country) {
        if (country == null) {
            return null;
        }

        return new CountryRepresentationDTO(country.getId(), country.getName());
    }

    public static DestinationCreateResponseDTO toDestinationDTO(Destination destination) {
        if (destination == null) {
            return null;
        }

        CountryRepresentationDTO countryRepresentationDTO = toCountryDTO(destination.getCountry());

        return new DestinationCreateResponseDTO(destination.getId(), destination.getName(), countryRepresentationDTO);
    }
}
