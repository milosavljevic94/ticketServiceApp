package com.ftn.dtos;

import com.ftn.enums.ManifestationCategory;
import com.ftn.model.Manifestation;

import java.time.LocalDateTime;

public class ManifestationDto {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ManifestationCategory manifestationCategory;

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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
