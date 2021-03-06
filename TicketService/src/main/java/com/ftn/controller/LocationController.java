package com.ftn.controller;

import com.ftn.dtos.LocationDto;
import com.ftn.dtos.ManifestationInfoDto;
import com.ftn.model.Location;
import com.ftn.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping(value = "/allLocation")
    public ResponseEntity<List<LocationDto>> getAllLocations(){

        List<LocationDto> locationDtos = locationService.allToDto();

        return new ResponseEntity<>(locationDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long id) {

        return new ResponseEntity<>(new LocationDto(locationService.findOneLocation(id)), HttpStatus.OK);
    }

    @GetMapping(value = "getLocationManifestations/{idLocation}")
    public ResponseEntity<List<ManifestationInfoDto>> getManifestationsOfLocation(@PathVariable Long idLocation) {

        List<ManifestationInfoDto> mdtos = locationService.getManifestationsOfLocation(idLocation);

        return new ResponseEntity<>(mdtos, HttpStatus.OK);
    }

    @PostMapping(value = "/addLocation", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocationDto> addLocationAndAddress(@RequestBody LocationDto locationDto) {

        Location l = locationService.addLocationAndAddress(locationDto);

        return new ResponseEntity<>(new LocationDto(l) , HttpStatus.CREATED);
    }



    @PutMapping(value = "/updateLocation", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocationDto> updateLocation(@RequestBody LocationDto locationDto){

        Location l = locationService.updateLocation(locationDto);

        return new ResponseEntity<>(new LocationDto(l), HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id){

        locationService.deleteLocation(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
