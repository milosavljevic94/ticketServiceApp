package com.ftn.controller;

import com.ftn.dtos.*;
import com.ftn.model.Manifestation;
import com.ftn.service.ManifestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/manifestation")
public class ManifestationController {

    @Autowired
    ManifestationService manifestationService;


    @GetMapping(value = "/allManifestations")
    public ResponseEntity<List<ManifestationDto>> getAllManifestations() {

        List<ManifestationDto> manifestationDtos = manifestationService.allToDto();

        return new ResponseEntity<>(manifestationDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ManifestationDto> getManifestation(@PathVariable Long id) {

        return new ResponseEntity<>(new ManifestationDto(manifestationService.findOneManifestation(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/manifestationDay/{id}")
    public ResponseEntity<ManifestationDayDto> getManifestationDay(@PathVariable Long id) {

        return new ResponseEntity<>(manifestationService.getManifestationDay(id), HttpStatus.OK);
    }


    @GetMapping(value = "/manifestationInfo/{id}")
    public ResponseEntity<ManifestationInfoDto> getManifestationInfoById(@PathVariable Long id) {


        return new ResponseEntity<>(new ManifestationInfoDto(manifestationService.findOneManifestation(id)), HttpStatus.OK);
    }


    @PostMapping(value = "/addManifestation", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ManifestationDto> addManifestation(@RequestBody ManifestationDto manifestationDto) {

        Manifestation m = manifestationService.addManifestation(manifestationDto);

        return new ResponseEntity<>(new ManifestationDto(m), HttpStatus.CREATED);
    }


    @PutMapping(value = "/updateManifestation", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ManifestationDto> updateManifestation(@RequestBody ManifestationDto mdto) {

        Manifestation m = manifestationService.updateManifestation(mdto);

        return new ResponseEntity<>(manifestationService.mapToDTO(m), HttpStatus.OK);
    }


    @PutMapping(value = "/addSectorPrice/{manifestationId}", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ManifestationInfoDto> setSectorPriceForManifestation(
            @PathVariable Long manifestationId, @RequestBody ManifestationSectorPriceDto sectorPriceDto) {

        Manifestation m = manifestationService.setPriceForSectorAndDay(manifestationId, sectorPriceDto);

        return new ResponseEntity<>(new ManifestationInfoDto(m), HttpStatus.OK);
    }

    /*
        May be used during project development.
    */
    @GetMapping(value = "/manifestationPrices/{id}")
    public ResponseEntity<List<ManifestationSectorPriceDto>> getManifestationPrices(@PathVariable Long id) {

        List<ManifestationSectorPriceDto> prices = manifestationService.getPricesForManifestation(id);

        return new ResponseEntity<>(prices, HttpStatus.OK);
    }

    /*
        May be used during project development.
    */
    @DeleteMapping(value = "deleteDay/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteManifestationDay(@PathVariable Long id) {

        manifestationService.deleteManifestationDay(id);

        return new ResponseEntity<>("Manifestation day deleted successfully!", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteManifestation(@PathVariable Long id) {

        manifestationService.deleteManifestation(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/updateManDay/{manId}", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ManifestationDto> updateManifestationDay(@PathVariable Long manId, @RequestBody ManifestationDaysDto daysDto) {
        Manifestation m = manifestationService.updateManifestationDay(manId, daysDto);

        return new ResponseEntity<>(manifestationService.mapToDTO(m), HttpStatus.OK);
    }

    @GetMapping(value = "/verifySeat/{idManDay}/{idSector}")
    public ResponseEntity<VerifyedSeats> verifySeat(@PathVariable("idManDay") Long idManDay, @PathVariable("idSector") Long idSector) {

        VerifyedSeats seats = manifestationService.getVerifiedSeats(idManDay, idSector);

        return new ResponseEntity<VerifyedSeats>(seats, HttpStatus.OK);
    }

}