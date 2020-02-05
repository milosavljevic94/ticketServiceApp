package com.ftn.model;

import com.ftn.enums.ManifestationCategory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manifestation")
public class Manifestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ManifestationCategory manifestationCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "manifestation")
    private Set<ManifestationDays> manifestationDays = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public Manifestation() {
    }

    public Manifestation(String name, String description, LocalDateTime startTime, LocalDateTime endTime, ManifestationCategory manifestationCategory, Set<ManifestationDays> manifestationDays, Location location) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.manifestationCategory = manifestationCategory;
        this.manifestationDays = manifestationDays;
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

    public Set<ManifestationDays> getManifestationDays() {
        return manifestationDays;
    }

    public void setManifestationDays(Set<ManifestationDays> manifestationDays) {
        this.manifestationDays = manifestationDays;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Manifestation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", manifestationCategory=" + manifestationCategory +
                ", manifestationDays=" + manifestationDays +
                ", location=" + location +
                '}';
    }
}
