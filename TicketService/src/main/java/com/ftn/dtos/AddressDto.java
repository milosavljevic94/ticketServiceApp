package com.ftn.dtos;

import com.ftn.model.Address;

public class AddressDto {

    private Long id;

    private String state;

    private String city;

    private String street;

    private Integer number;

    private Double latitude;

    private Double longitude;

    public AddressDto() {
    }

    public AddressDto(Address address) {

        this.id = address.getId();
        this.state = address.getState();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "AddressDto{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
