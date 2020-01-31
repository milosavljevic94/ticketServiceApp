package com.ftn.dtos;

import com.ftn.model.Location;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationDto {

    private Long id;

    private String locationName;

    @NotNull
    private AddressDto address;

    private Set<SectorDto> sectors;

    private Set<ManifestationDto> manifestations;

    public LocationDto() {
    }

    public LocationDto(Location location) {
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
        this.sectors = new HashSet(location.getSectors().stream().
                                    map(sector -> new SectorDto(sector)).collect(Collectors.toSet()));
        this.manifestations = new HashSet(location.getManifestations().stream().
                                    map(manifestation -> new ManifestationDto(manifestation)).collect(Collectors.toSet()));
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

    public Set<SectorDto> getSectors() {
        return sectors;
    }

    public void setSectors(Set<SectorDto> sectors) {
        this.sectors = sectors;
    }

    public Set<ManifestationDto> getManifestations() {
        return manifestations;
    }

    public void setManifestations(Set<ManifestationDto> manifestations) {
        this.manifestations = manifestations;
    }
}
