package com.ftn.dtos;

import java.time.LocalDateTime;

public class ManifestationDayDto {

	private Long id;
	private ManifestationDto manifestation;
	private String name;
	private LocalDateTime startTime;
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
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	
	
}
