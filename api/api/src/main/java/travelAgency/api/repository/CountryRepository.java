package travelAgency.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelAgency.api.model.Country;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {
}
