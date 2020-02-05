package com.ftn.service;


import com.ftn.dtos.AddressDto;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Address;
import com.ftn.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    public List<Address> finfAllAddress(){
        return addressRepository.findAll();
    }

    public Address findOneAddress(Long id){
        try {
            return addressRepository.findById(id).orElse(null);
        }catch (NoSuchElementException e){
            throw new EntityNotFoundException("Address with id : "+ id +" not found.");
        }
    }

    public Optional<Address> findOneAddressOptional(Long id){
        return addressRepository.findById(id);
    }

    public void addAddress(Address r){
        addressRepository.save(r);
    }

    public void deleteAddress(Long id){
        addressRepository.deleteById(id);
    }

    public void deleteAll(){
        addressRepository.deleteAll();
    }

    public Boolean ifExist(Long id){
        return addressRepository.existsById(id);
    }

    public void updateAddress(AddressDto addressDto) {

        Address address = findOneAddress(addressDto.getId());

        address.setState(addressDto.getState());
        address.setCity(addressDto.getCity());
        address.setStreet(addressDto.getStreet());
        address.setNumber(addressDto.getNumber());
        address.setLongitude(addressDto.getLongitude());
        address.setLatitude(addressDto.getLatitude());

        addressRepository.save(address);
    }


    public AddressDto mapToDTO(Address address){

        AddressDto addressDto = new AddressDto(address);

        return addressDto;
    }

    public List<AddressDto> allToDto() {

        List<Address> addresses = finfAllAddress();
        List<AddressDto> addressDtos = new ArrayList<>();

        for (Address a : addresses) {
            addressDtos.add(mapToDTO(a));
        }
        return addressDtos;
    }

    public Address mapFromDto(AddressDto addressDto) {

        Address address = new Address();

        address.setId(addressDto.getId());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setStreet(addressDto.getStreet());
        address.setNumber(addressDto.getNumber());
        address.setLatitude(addressDto.getLatitude());
        address.setLongitude(addressDto.getLongitude());

        return address;
    }


}
