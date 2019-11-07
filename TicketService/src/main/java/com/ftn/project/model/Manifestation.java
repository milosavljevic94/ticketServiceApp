package com.ftn.project.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "manifestation")
public class Manifestation {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	private String name;
	
	private String description;
	
	private Date startDateTime;
	
	@OneToMany(mappedBy = "manifestation", cascade = CascadeType.ALL, orphanRemoval = true)
	Set<ManDays> days;
	
	@ManyToOne
	private Location location;
	
	@ManyToMany
	Set<Category> categories;
	
	public Manifestation() {
		super();
	}

	public Manifestation(Long id, String name, String description, Date startDateTime, Set<ManDays> days,
			Location location) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDateTime = startDateTime;
		this.days = days;
		this.location = location;
	}
	
	public Manifestation(Long id, String name, String description, Date startDateTime, Set<ManDays> days,
			Location location, Set<Category> categories) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDateTime = startDateTime;
		this.days = days;
		this.location = location;
		this.categories = categories;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Set<ManDays> getDays() {
		return days;
	}

	public void setDays(Set<ManDays> days) {
		this.days = days;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
}
