package com.ftn.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sector")
public class Sector {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	private String name;
	
	private int numOfSeats;
	
	private int colNum;
	
	private int rowNum;
	
	@ManyToOne
	private Location location;
	
	public Sector() {
		super();
	}

	public Sector(Long id, String name, int numOfSeats, int colNum, int rowNum, Location location) {
		super();
		this.id = id;
		this.name = name;
		this.numOfSeats = numOfSeats;
		this.colNum = colNum;
		this.rowNum = rowNum;
		this.location = location;
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

	public int getNumOfSeats() {
		return numOfSeats;
	}

	public void setNumOfSeats(int numOfSeats) {
		this.numOfSeats = numOfSeats;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int columns) {
		this.colNum = columns;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rows) {
		this.rowNum = rows;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
