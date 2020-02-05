package com.ftn.dtos;

import com.ftn.model.Sector;

public class SectorDto {

    private Long id;

    private String sectorName;

    private Integer seatsNumber;

    private Integer rows;

    private Integer columns;

    private Long locationId;

    public SectorDto() {
    }

    public  SectorDto(Sector sector) {
        this.id = sector.getId();
        this.sectorName = sector.getSectorName();
        this.seatsNumber = sector.getSeatsNumber();
        this.rows = sector.getRows();
        this.columns = sector.getColumns();
        this.locationId = sector.getLocation().getId();
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
