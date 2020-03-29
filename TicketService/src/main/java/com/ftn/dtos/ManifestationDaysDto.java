package com.ftn.dtos;

import com.ftn.model.ManifestationDays;

import java.time.LocalDateTime;
import java.util.Collections;

public class ManifestationDaysDto {

	private Long id;
	private ManifestationDto manifestation;
	private String name;
	private String description;
	private LocalDateTime startTime;

    public ManifestationDaysDto() {
    }

    public ManifestationDaysDto(ManifestationDays md) {
        this.id = md.getId();
            ManifestationDto manifestationDto = new ManifestationDto();
            manifestationDto.setId(md.getManifestation().getId());
            manifestationDto.setName(md.getManifestation().getName());
            manifestationDto.setDescription(md.getManifestation().getDescription());
            manifestationDto.setEndTime(md.getManifestation().getEndTime());
            manifestationDto.setStartTime(md.getManifestation().getStartTime());
            manifestationDto.setManifestationCategory(md.getManifestation().getManifestationCategory());
            manifestationDto.setLocationId(md.getManifestation().getLocation().getId());
            manifestationDto.setManDaysDto(Collections.emptySet());
        this.manifestation = manifestationDto;
        this.name = md.getName();
        this.description = md.getDescription();
        this.startTime = md.getStartTime();
    }

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ManifestationDto getManifestation() {
		return manifestation;
	}
	public void setManifestation(ManifestationDto manifestation) {
		this.manifestation = manifestation;
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
	
	
}
