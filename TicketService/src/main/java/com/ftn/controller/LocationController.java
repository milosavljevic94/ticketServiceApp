package com.ftn.controller;

import com.ftn.dtos.LocationDto;
import com.ftn.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/addLocation", consumes = "application/json")
    public ResponseEntity<LocationDto> addLocationAndAddress(@RequestBody LocationDto addressDto) {

        locationService.addLocationAndAddress(addressDto);

        return new ResponseEntity<>(new LocationDto(locationService.mapFromDto(addressDto)) , HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateLocation", consumes = "application/json")
    public ResponseEntity<LocationDto> updateLocation(@RequestBody LocationDto locationDto){

        locationService.updateLocation(locationDto);

        return new ResponseEntity<>(new LocationDto(locationService.mapFromDto(locationDto)), HttpStatus.OK);
    }

    @PutMapping(value = "/updateLocationAddress", consumes = "application/json")
    public ResponseEntity<LocationDto> updateLocationAddress(@RequestBody LocationDto locationDto){

        locationService.updateAddress(locationDto.getId(),locationDto.getAddress());

        return new ResponseEntity<>(new LocationDto(locationService.mapFromDto(locationDto)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id){

        locationService.deleteLocation(id);

        return new ResponseEntity<>("Location deleted successfully!", HttpStatus.OK);
    }
}
