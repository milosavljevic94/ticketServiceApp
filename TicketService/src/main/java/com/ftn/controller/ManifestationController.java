package com.ftn.controller;

import com.ftn.dtos.ManifestationDto;
import com.ftn.service.ManifestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/manifestation")
public class ManifestationController {

    @Autowired
    ManifestationService manifestationService;


    @GetMapping(value = "/allManifestations")
    public ResponseEntity<List<ManifestationDto>> getAllManifestations(){

        List<ManifestationDto> manifestationDtos = manifestationService.allToDto();

        return new ResponseEntity<>(manifestationDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ManifestationDto> getManifestation(@PathVariable Long id) {

        return new ResponseEntity<>(new ManifestationDto(manifestationService.findOneManifestation(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/addManifestation", consumes = "application/json")
    public ResponseEntity<ManifestationDto> addManifestation(@RequestBody ManifestationDto manifestationDto) {

        manifestationService.addManifestation(manifestationDto);

        return new ResponseEntity<>(new ManifestationDto(manifestationService.mapFromDto(manifestationDto)) , HttpStatus.CREATED);
    }
}
