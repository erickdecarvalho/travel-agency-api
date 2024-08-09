package travelAgency.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import travelAgency.api.dto.CountryRepresentationDTO;
import travelAgency.api.dto.DestinationCreateResponseDTO;
import travelAgency.api.dto.DestinationInputFormDTO;
import travelAgency.api.mapper.EntityToDTOMapper;
import travelAgency.api.model.Country;
import travelAgency.api.model.Destination;
import travelAgency.api.model.Travel;
import travelAgency.api.repository.CountryRepository;
import travelAgency.api.repository.DestinationRepository;
import travelAgency.api.repository.TravelRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/destinos")
public class DestinationController {
    private final DestinationRepository destinationRepository;
    private final CountryRepository countryRepository;
    private final TravelRepository travelRepository;

    public DestinationController(DestinationRepository destinationRepository, CountryRepository countryRepository, TravelRepository travelRepository) {
        this.destinationRepository = destinationRepository;
        this.countryRepository = countryRepository;
        this.travelRepository = travelRepository;
    }

    @GetMapping
    public ResponseEntity<List<DestinationCreateResponseDTO>> getAllDestinations() {
        List<Destination> destinations = destinationRepository.findAll();

        List<DestinationCreateResponseDTO> destinationDTOs = destinations.stream()
                .map(destination -> new DestinationCreateResponseDTO(
                        destination.getId(),
                        destination.getName(),
                        new CountryRepresentationDTO(
                                destination.getCountry().getId(),
                                destination.getCountry().getName()
                        )
                ))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(destinationDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationCreateResponseDTO> getDestinationById(@PathVariable UUID id) {
        Optional<Destination> optionalDestination = destinationRepository.findById(id);

        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();

            DestinationCreateResponseDTO destinationDTO = new DestinationCreateResponseDTO(
                    destination.getId(),
                    destination.getName(),
                    new CountryRepresentationDTO(
                            destination.getCountry().getId(),
                            destination.getCountry().getName()
                    )
            );

            return ResponseEntity.status(HttpStatus.OK).body(destinationDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DestinationCreateResponseDTO> createDestination(@RequestBody @Valid DestinationInputFormDTO destinationInputFormDTO) {
        UUID countryId = destinationInputFormDTO.countryId();
        if (countryId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Country> optionalCountry = countryRepository.findById(countryId);
        if (optionalCountry.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Country country = optionalCountry.get();

        Destination entity = new Destination();
        entity.setName(destinationInputFormDTO.name());
        entity.setCountry(country);

        Destination savedEntity = destinationRepository.save(entity);

        DestinationCreateResponseDTO destinationResponseDTO = EntityToDTOMapper.toDestinationDTO(savedEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(destinationResponseDTO);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteDestination(@PathVariable(value="id") UUID id){
        Optional<Destination> destination = destinationRepository.findById(id);
        if(destination.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destino nao encontrado");
        }else {
            destinationRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Destino excluído com sucesso!");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDestination(@PathVariable(value = "id") UUID id, @RequestBody @Valid DestinationInputFormDTO destinationInputFormDTO){
        Optional<Destination> destination = destinationRepository.findById(id);
        if(destination.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product nao encontrado");
        }else {
            var destinationModel = destination.get();
            BeanUtils.copyProperties(destinationInputFormDTO, destinationModel);

            Destination savedDestination = destinationRepository.save(destinationModel);

            DestinationCreateResponseDTO responseDTO = new DestinationCreateResponseDTO(
                    savedDestination.getId(),
                    savedDestination.getName(),
                    new CountryRepresentationDTO(
                            savedDestination.getCountry().getId(),
                            savedDestination.getCountry().getName()
                    )
            );

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        }
    }

    /*
    * Funcionalidade: Esse endpoint retorna o destino mais viajado
    */
    @GetMapping("/mais-viajado")
    public ResponseEntity<DestinationCreateResponseDTO> getDestinationWithMostTravels() {
        List<Travel> travels = travelRepository.findAll();

        Map<Destination, Long> destinationCountMap = travels.stream()
                .collect(Collectors.groupingBy(Travel::getDestination, Collectors.counting()));

        Optional<Destination> mostPopularDestination = destinationCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        if (mostPopularDestination.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Destination destination = mostPopularDestination.get();
        DestinationCreateResponseDTO destinationDTO = new DestinationCreateResponseDTO(
                destination.getId(),
                destination.getName(),
                new CountryRepresentationDTO(
                        destination.getCountry().getId(),
                        destination.getCountry().getName()
                )
        );

        return ResponseEntity.status(HttpStatus.OK).body(destinationDTO);
    }
}
