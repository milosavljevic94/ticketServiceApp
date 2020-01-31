package com.ftn.dtos;

import com.ftn.model.ManifestationDays;

import java.time.LocalDateTime;

public class ManifestationDaysDto {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime startTime;

    private Long manifestationId;

    public ManifestationDaysDto() {
    }

    public ManifestationDaysDto(ManifestationDays days) {
        this.id = days.getId();
        this.name = days.getName();
        this.description = days.getDescription();
        this.startTime = days.getStartTime();
        this.manifestationId = days.getManifestation().getId();
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

    public Long getManifestationId() {
        return manifestationId;
    }

    public void setManifestationId(Long manifestationId) {
        this.manifestationId = manifestationId;
    }
}
