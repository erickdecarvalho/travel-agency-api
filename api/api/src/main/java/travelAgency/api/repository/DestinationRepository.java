package travelAgency.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelAgency.api.model.Destination;

import java.util.UUID;

public interface DestinationRepository extends JpaRepository<Destination, UUID> {
}
