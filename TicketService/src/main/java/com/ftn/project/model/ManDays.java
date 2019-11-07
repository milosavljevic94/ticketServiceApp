package com.ftn.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "man_days")
public class ManDays {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	private String name;
	
	@ManyToOne
	private Manifestation manifestation;
	
	public ManDays() {
		super();
	}

	public ManDays(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
}
