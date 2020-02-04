package com.ftn.dtos;

import com.ftn.model.Location;

public class LocationDtoBasicInfo {

    private Long id;

    private String locationName;

    private AddressDto address;

    public LocationDtoBasicInfo() {
    }

    public LocationDtoBasicInfo(Location location){

        this.id = location.getId();
        this.locationName = location.getLocationName();
        AddressDto addressDto = new AddressDto();
        addressDto.setId(location.getAddress().getId());
        addressDto.setState(location.getAddress().getState());
        addressDto.setCity(location.getAddress().getCity());
        addressDto.setStreet(location.getAddress().getStreet());
        addressDto.setCity(location.getAddress().getCity());
        addressDto.setNumber(location.getAddress().getNumber());
        addressDto.setLatitude(location.getAddress().getLatitude());
        addressDto.setLongitude(location.getAddress().getLongitude());
        this.address = addressDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
