package travelAgency.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import travelAgency.api.dto.CountryInputFormDTO;
import travelAgency.api.model.Country;
import travelAgency.api.repository.CountryRepository;

import java.util.List;

@RestController
@RequestMapping("/paises")
public class CountryController {
    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries(){
        return ResponseEntity.status(HttpStatus.OK).body(countryRepository.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Country> addCountry(@RequestBody @Valid CountryInputFormDTO countryInputFormDTO){
        var country =  new Country();
        BeanUtils.copyProperties(countryInputFormDTO, country);
        return ResponseEntity.status(HttpStatus.CREATED).body(countryRepository.save(country));
    }
}
