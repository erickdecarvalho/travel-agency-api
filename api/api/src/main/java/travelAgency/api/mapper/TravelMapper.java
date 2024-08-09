package travelAgency.api.mapper;

import travelAgency.api.dto.CountryRepresentationDTO;
import travelAgency.api.dto.DestinationCreateResponseDTO;
import travelAgency.api.dto.TravelCreateResponseDTO;
import travelAgency.api.model.Travel;

public class TravelMapper {

    public static TravelCreateResponseDTO convertToDTO(Travel travel) {
        return new TravelCreateResponseDTO(
                travel.getId(),
                travel.getTitle(),
                new DestinationCreateResponseDTO(
                        travel.getDestination().getId(),
                        travel.getDestination().getName(),
                        new CountryRepresentationDTO(
                                travel.getDestination().getCountry().getId(),
                                travel.getDestination().getCountry().getName()
                        )
                ),
                travel.getPrice(),
                travel.getStartDate(),
                travel.getEndDate()
        );
    }
}
