package com.ftn.controller;

import com.ftn.dtos.SectorDto;
import com.ftn.model.Sector;
import com.ftn.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/sector")
public class SectorController {

    @Autowired
    SectorService sectorService;

    @GetMapping(value = "/allSectorsForLocation/{id}")
    public ResponseEntity<List<SectorDto>> getAllSectorsForLocation(@PathVariable Long id){

        List<SectorDto> sectorDtos = sectorService.allSectorsForLocation(id);

        return new ResponseEntity<>(sectorDtos, HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<SectorDto> getOneSector(@PathVariable Long id) {

        return new ResponseEntity<>(new SectorDto(sectorService.findOneSector(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/addSector", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SectorDto> addSector(@RequestBody SectorDto sd) {

        Sector sector = sectorService.addSector(sd);

        return new ResponseEntity<>(sectorService.mapToDTO(sector) , HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateSector", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SectorDto> updateAddress(@RequestBody SectorDto sd){

        Sector s = sectorService.updateSector(sd);

        return new ResponseEntity<>(sectorService.mapToDTO(s), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSector(@PathVariable Long id){

        sectorService.deleteSector(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
