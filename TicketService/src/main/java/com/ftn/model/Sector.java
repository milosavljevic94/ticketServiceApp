package com.ftn.model;

import com.ftn.dtos.SectorDto;

import javax.persistence.*;

@Entity
@Table(name = "sector")
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sectorName;

    private Integer seatsNumber;

    private Integer rows;

    private Integer columns;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public Sector() {
    }

    public Sector(String sectorName, Integer rows, Integer columns, Location location) {
        this.sectorName = sectorName;
        this.rows = rows;
        this.columns = columns;
        this.seatsNumber = rows*columns;
        this.location = location;
    }

    public Sector(SectorDto sectorDto){

        this.sectorName = sectorDto.getSectorName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public Integer getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(Integer seatsNumber) {
        this.seatsNumber = seatsNumber;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Sector{" +
                "id=" + id +
                ", sectorName='" + sectorName + '\'' +
                ", seatsNumber=" + seatsNumber +
                ", rows=" + rows +
                ", columns=" + columns +
                ", location=" + location.getLocationName() +
                '}';
    }
}
