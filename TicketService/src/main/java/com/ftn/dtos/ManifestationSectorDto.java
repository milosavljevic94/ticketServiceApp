package com.ftn.dtos;

import com.ftn.model.ManifestationSector;

public class ManifestationSectorDto {

    private Long id;

    private Long sectorId;

    private Double price;

    public ManifestationSectorDto() {
    }

    public ManifestationSectorDto(ManifestationSector manifestationSector) {
        this.id = manifestationSector.getId();
        this.sectorId = manifestationSector.getSector().getId();
        this.price = manifestationSector.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
