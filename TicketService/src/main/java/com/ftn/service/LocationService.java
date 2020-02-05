package com.ftn.service;

import com.ftn.dtos.AddressDto;
import com.ftn.dtos.LocationDto;
import com.ftn.dtos.SectorDto;
import com.ftn.exceptions.EntityAlreadyExistException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Address;
import com.ftn.model.Location;
import com.ftn.model.Sector;
import com.ftn.repository.AddressRepository;
import com.ftn.repository.LocationRepository;
import com.ftn.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    SectorService sectorService;

    @Autowired
    SectorRepository sectorRepository;

    public List<Location> finfAllLocation(){
        return locationRepository.findAll();
    }

    public Location findOneLocation(Long id){

        try {
            return locationRepository.findById(id).orElse(null);
        }catch (NoSuchElementException e){
            throw new EntityNotFoundException("Location with id : "+ id +" not found.");
        }
    }

    public Optional<Location> findOneLocationOptional(Long id){
        return locationRepository.findById(id);
    }

    public void addLocation(Location l){
        locationRepository.save(l);
    }

    public void addLocationAndAddress(LocationDto locationDto){


        if(locationRepository.findByLocationName(locationDto.getLocationName()) != null){
            throw new EntityAlreadyExistException("Location with that name already exist!");
        }

        Address address = mapFromDto(locationDto).getAddress();
        addressRepository.save(address);



        Location location = mapFromDto(locationDto);
        location.setAddress(address);

        locationRepository.save(location);
    }

    public Location addSectorToLocation(Long id, SectorDto sd){

        Location l = findOneLocationOptional(id).orElse(null);

        Sector s = sectorService.mapFromDto(sd);

        l.getSectors().add(s);
        s.setLocation(l);

        sectorRepository.save(s);

        return l;
    }

    public void deleteLocation(Long id){
        locationRepository.deleteById(id);
    }

    public void deleteAll(){
        locationRepository.deleteAll();
    }

    public Boolean ifExist(Long id){
        return locationRepository.existsById(id);
    }

    public void updateLocation(LocationDto locationDto) {

        Location location = findOneLocation(locationDto.getId());

        location.setLocationName(locationDto.getLocationName());

        locationRepository.save(location);
    }

    /*
        Parameters: ID of location
                    Address to change.
     */
    public void updateAddress(Long id, AddressDto addressDto){

        Location l = findOneLocation(id);

        l.getAddress().setState(addressDto.getState());
        l.getAddress().setCity(addressDto.getCity());
        l.getAddress().setStreet(addressDto.getStreet());
        l.getAddress().setNumber(addressDto.getNumber());
        l.getAddress().setLatitude(addressDto.getLatitude());
        l.getAddress().setLongitude(addressDto.getLongitude());

        addressRepository.save(l.getAddress());



        locationRepository.save(l);
    }


    public LocationDto mapToDTO(Location location){

        LocationDto locationDto = new LocationDto(location);

        return locationDto;
    }

    public List<LocationDto> allToDto() {

        List<Location> locationes = finfAllLocation();
        List<LocationDto> locationDtos = new ArrayList<>();

        for (Location l : locationes) {
            locationDtos.add(mapToDTO(l));
        }
        return locationDtos;
    }

    public Location mapFromDto(LocationDto locationDto) {

        Location location = new Location();

        location.setLocationName(locationDto.getLocationName());
        location.setAddress(addressService.mapFromDto(locationDto.getAddress()));

        return location;
    }
}
