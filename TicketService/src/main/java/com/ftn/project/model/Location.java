package com.ftn.project.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location {
	
	@Id
	@GeneratedValue
	@Column(name = "location_id")
	private Long id;
	
	@Column(name = "name")
	String nameOfLocation;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", referencedColumnName = "id")
	Address address;
	
	@OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
	Set<Sector> sectors;
	
	@OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
	Set<Manifestation> manifestations;

	public Location() {
		super();
	}
	
	public Location(Long id, String nameOfLocation, Address address, Set<Sector> sectors,
			Set<Manifestation> manifestations) {
		super();
		this.id = id;
		this.nameOfLocation = nameOfLocation;
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

	public String getNameOfLocation() {
		return nameOfLocation;
	}

	public void setNameOfLocation(String nameOfLocation) {
		this.nameOfLocation = nameOfLocation;
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
	
}
