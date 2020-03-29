package com.ftn.dtos;

import com.ftn.enums.ManifestationCategory;
import com.ftn.model.Manifestation;
import com.ftn.model.ManifestationDays;
import com.ftn.model.ManifestationSector;

import java.util.ArrayList;
import java.util.List;

public class ManifestationInfoDto {

    private Long id;

    private String manifestationName;

    private String description;

    private String startDate;

    private String endDate;

    private ManifestationCategory category;

    private List<ManifestationDaysDto> days;

    private List<ManifestationSectorDto> sectorPrices;

    private LocationDtoBasicInfo location;

    public ManifestationInfoDto() {
    }

    public ManifestationInfoDto(Manifestation m){
        this.id = m.getId();
        this.manifestationName = m.getName();
        this.description = m.getDescription();
        this.startDate = m.getStartTime().toString();
        this.endDate = m.getEndTime().toString();
        this.category = m.getManifestationCategory();

        List<ManifestationDaysDto> mdDtos = new ArrayList<>();
        for(ManifestationDays md : m.getManifestationDays()){
            ManifestationDaysDto mdDto = new ManifestationDaysDto();
            mdDto.setId(md.getId());
            mdDto.setName(md.getName());
            mdDto.setDescription(md.getDescription());
            mdDto.setStartTime(md.getStartTime());
            mdDtos.add(mdDto);
        }
        this.days =mdDtos;

        List<ManifestationSectorDto> msDtos = new ArrayList<>();
        for (ManifestationDays md : m.getManifestationDays()){
            for(ManifestationSector ms : md.getManifestationSectors()){
                ManifestationSectorDto msDto = new ManifestationSectorDto();
                msDto.setId(ms.getId());
                msDto.setSectorId(ms.getSector().getId());
                msDto.setManDayId(ms.getManifestationDays().getId());
                msDto.setPrice(ms.getPrice());
                msDtos.add(msDto);
            }
        }
        this.sectorPrices = msDtos;
        this.location = new LocationDtoBasicInfo(m.getLocation());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManifestationName() {
        return manifestationName;
    }

    public void setManifestationName(String manifestationName) {
        this.manifestationName = manifestationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ManifestationCategory getCategory() {
        return category;
    }

    public void setCategory(ManifestationCategory category) {
        this.category = category;
    }

    public List<ManifestationDaysDto> getDays() {
        return days;
    }

    public void setDays(List<ManifestationDaysDto> days) {
        this.days = days;
    }

    public List<ManifestationSectorDto> getSectorPrices() {
        return sectorPrices;
    }

    public void setSectorPrices(List<ManifestationSectorDto> sectorPrices) {
        this.sectorPrices = sectorPrices;
    }

    public LocationDtoBasicInfo getLocation() {
        return location;
    }

    public void setLocation(LocationDtoBasicInfo location) {
        this.location = location;
    }
}
