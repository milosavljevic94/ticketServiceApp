package com.ftn.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "location")
public class    Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locationName;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "location", orphanRemoval = true)
    private Set<Sector> sectors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "location")
    private Set<Manifestation> manifestations = new HashSet<>();

    public Location() {
    }

    public Location(String locationName, Address address, Set<Sector> sectors, Set<Manifestation> manifestations) {
        this.locationName = locationName;
        this.address = address;
        this.sectors = sectors;
        this.manifestations = manifestations;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Sector> getSectors() {
        return sectors;
    }

    public void setSectors(Set<Sector> sectors) {
        this.sectors = sectors;
    }

    public Set<Manifestation> getManifestations() {
        return manifestations;
    }

    public void setManifestations(Set<Manifestation> manifestations) {
        this.manifestations = manifestations;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", locationName='" + locationName + '\'' +
                ", address=" + address +
                ", sectors=" + sectors +
                ", manifestations=" + manifestations.stream().map(manifestation -> manifestation.getName()) +
                '}';
    }
}
