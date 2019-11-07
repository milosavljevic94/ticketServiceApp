package com.ftn.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	private String state;
	
	private String city;
	
	private String street;
	
	private int number;
	
	private Double lat;
	
	private Double lng;
	
	@OneToOne(mappedBy = "address")
	private Location location;
	
	public Address() {
		super();
	}

	public Address(Long id, String state, String city, String street, int number, Double lat, Double lng,
			Location location) {
		super();
		this.id = id;
		this.state = state;
		this.city = city;
		this.street = street;
		this.number = number;
		this.lat = lat;
		this.lng = lng;
		this.location = location;
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
