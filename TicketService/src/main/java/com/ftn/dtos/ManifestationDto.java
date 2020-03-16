package com.ftn.dtos;

import com.ftn.enums.ManifestationCategory;
import com.ftn.model.Manifestation;
import com.ftn.model.ManifestationDays;
import com.ftn.model.ManifestationSector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManifestationDto {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ManifestationCategory manifestationCategory;

    private Set<ManifestationDaysDto> manDaysDto;

    private Long locationId;

    public ManifestationDto() {
    }

    public ManifestationDto(Manifestation manifestation) {
        this.id = manifestation.getId();
        this.name = manifestation.getName();
        this.description = manifestation.getDescription();
        this.startTime = manifestation.getStartTime();
        this.endTime = manifestation.getEndTime();
        this.manifestationCategory = manifestation.getManifestationCategory();

            Set<ManifestationDaysDto> manDaysDtos = new HashSet<>();
            for(ManifestationDays md : manifestation.getManifestationDays()){
                ManifestationDaysDto manDayDto = new ManifestationDaysDto();
                manDayDto.setId(md.getId());
                manDayDto.setName(md.getName());
                manDayDto.setDescription(md.getDescription());
                manDayDto.setStartTime(md.getStartTime());
                List<ManifestationSectorPriceDto> pricesDtos = new ArrayList<>();
                for(ManifestationSector ms : md.getManifestationSectors()){
                    ManifestationSectorPriceDto priceDto = new ManifestationSectorPriceDto();
                    priceDto.setId(ms.getId());
                    priceDto.setPrice(ms.getPrice());
                    priceDto.setDayId(ms.getManifestationDays().getId());
                    priceDto.setSectorId(ms.getSector().getId());
                    pricesDtos.add(priceDto);
                }
                manDayDto.setSectorPricesDtos(pricesDtos);
                manDayDto.setManifestationId(manifestation.getId());
                manDaysDtos.add(manDayDto);
            }

        this.manDaysDto = manDaysDtos;
        this.locationId = manifestation.getLocation().getId();
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ManifestationCategory getManifestationCategory() {
        return manifestationCategory;
    }

    public void setManifestationCategory(ManifestationCategory manifestationCategory) {
        this.manifestationCategory = manifestationCategory;
    }

    public Set<ManifestationDaysDto> getManDaysDto() {
        return manDaysDto;
    }

    public void setManDaysDto(Set<ManifestationDaysDto> manDaysDto) {
        this.manDaysDto = manDaysDto;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
