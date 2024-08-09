package travelAgency.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import travelAgency.api.dto.TravelCreateResponseDTO;
import travelAgency.api.dto.TravelInputFormDTO;
import travelAgency.api.model.Destination;
import travelAgency.api.model.Travel;
import travelAgency.api.repository.DestinationRepository;
import travelAgency.api.repository.TravelRepository;
import travelAgency.api.mapper.TravelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/viagens")
public class TravelController {

    private final TravelRepository travelRepository;
    private final DestinationRepository destinationRepository;

    public TravelController(TravelRepository travelRepository, DestinationRepository destinationRepository) {
        this.travelRepository = travelRepository;
        this.destinationRepository = destinationRepository;
    }

    @GetMapping
    public ResponseEntity<List<TravelCreateResponseDTO>> getAllTravels() {
        List<Travel> travels = travelRepository.findAll();
        List<TravelCreateResponseDTO> travelsDTOs = travels.stream()
                .map(TravelMapper::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(travelsDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelCreateResponseDTO> getTravelById(@PathVariable UUID id) {
        return travelRepository.findById(id)
                .map(TravelMapper::convertToDTO)
                .map(dto -> ResponseEntity.ok(dto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping
    public ResponseEntity<TravelCreateResponseDTO> createTravel(@RequestBody @Valid TravelInputFormDTO travelInputFormDTO) {
        return destinationRepository.findById(travelInputFormDTO.destinationId())
                .map(destination -> {
                    Travel travel = new Travel();
                    BeanUtils.copyProperties(travelInputFormDTO, travel);
                    travel.setDestination(destination);
                    Travel savedTravel = travelRepository.save(travel);
                    return ResponseEntity.status(HttpStatus.CREATED).body(TravelMapper.convertToDTO(savedTravel));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelCreateResponseDTO> updateTravel(@PathVariable UUID id, @RequestBody @Valid TravelInputFormDTO travelInputFormDTO) {
        Optional<Travel> optionalTravel = travelRepository.findById(id);

        if (optionalTravel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Travel existingTravel = optionalTravel.get();
        BeanUtils.copyProperties(travelInputFormDTO, existingTravel, "id");

        if (travelInputFormDTO.destinationId() != null) {
            Optional<Destination> optionalDestination = destinationRepository.findById(travelInputFormDTO.destinationId());
            if (optionalDestination.isPresent()) {
                existingTravel.setDestination(optionalDestination.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        Travel updatedTravel = travelRepository.save(existingTravel);
        return ResponseEntity.ok(TravelMapper.convertToDTO(updatedTravel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravel(@PathVariable UUID id) {
        if (travelRepository.existsById(id)) {
            travelRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/destinos/{destinoId}")
    public ResponseEntity<List<TravelCreateResponseDTO>> getTravelsByDestination(@PathVariable UUID destinoId) {
        List<Travel> travels = travelRepository.findByDestinationId(destinoId);
        List<TravelCreateResponseDTO> travelDTOs = travels.stream()
                .map(TravelMapper::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(travelDTOs);
    }

    /*
    * Funcionalidade: Esse endpoint retorna a viagem mais barata de um destino
    */
    @GetMapping("/mais-barata/{destinoId}")
    public ResponseEntity<TravelCreateResponseDTO> getCheapestTravelByDestination(@PathVariable UUID destinoId) {
        return travelRepository.findByDestinationId(destinoId).stream()
                .min((t1, t2) -> Double.compare(t1.getPrice(), t2.getPrice()))
                .map(TravelMapper::convertToDTO)
                .map(dto -> ResponseEntity.ok(dto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
