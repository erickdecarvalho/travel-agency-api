package travelAgency.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelAgency.api.model.Travel;

import java.util.List;
import java.util.UUID;

public interface TravelRepository extends JpaRepository<Travel, UUID> {
        List<Travel> findByDestinationId(UUID destinationId);
    }
