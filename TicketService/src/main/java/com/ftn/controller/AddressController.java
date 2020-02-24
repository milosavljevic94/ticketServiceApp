package com.ftn.controller;

import com.ftn.dtos.AddressDto;
import com.ftn.model.Address;
import com.ftn.repository.LocationRepository;
import com.ftn.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    LocationRepository locationRepository;

    @GetMapping(value = "/allAddress")
    public ResponseEntity<List<AddressDto>> getAllAddress(){

        List<AddressDto> addressDtos = addressService.allToDto();

        return new ResponseEntity<>(addressDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long id) {

        return new ResponseEntity<>(new AddressDto(addressService.findOneAddress(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/addAddress", consumes = "application/json")
    public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto addressDto) {

        Address a =  addressService.addAddress(addressService.mapFromDto(addressDto));

        return new ResponseEntity<>(new AddressDto(a) , HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateAddress", consumes = "application/json")
    public ResponseEntity<AddressDto> updateAddress(@RequestBody AddressDto addressDto){

        addressService.updateAddress(addressDto);

        return new ResponseEntity<>(new AddressDto(addressService.mapFromDto(addressDto)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id){

        addressService.deleteAddress(id);

        return new ResponseEntity<>("Address deleted successfully!", HttpStatus.OK);
    }
}
