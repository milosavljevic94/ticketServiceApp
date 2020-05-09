package com.ftn.model;

import com.ftn.dtos.ManifestationDaysDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manifestation_days")
public class ManifestationDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime startTime;

    @ManyToOne
    @JoinColumn(name = "manifestation_id")
    private Manifestation manifestation;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "manifestationDays")
    private Set<Ticket> tickets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "manifestationDays")
    private Set<ManifestationSector> manifestationSectors = new HashSet<>();

    public ManifestationDays() {
    }

    public ManifestationDays(String name, String description, LocalDateTime startTime,
                             Manifestation manifestation, Set<Ticket> tickets,
                             Set<ManifestationSector> manifestationSectors) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.manifestation = manifestation;
        this.tickets = tickets;
        this.manifestationSectors = manifestationSectors;
    }

    public ManifestationDays(ManifestationDaysDto manifestationDaysDto) {
        this.name = manifestationDaysDto.getName();
        this.startTime = manifestationDaysDto.getStartTime();
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

    public Manifestation getManifestation() {
        return manifestation;
    }

    public void setManifestation(Manifestation manifestation) {
        this.manifestation = manifestation;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<ManifestationSector> getManifestationSectors() {
        return manifestationSectors;
    }

    public void setManifestationSectors(Set<ManifestationSector> manifestationSectors) {
        this.manifestationSectors = manifestationSectors;
    }

    @Override
    public String toString() {
        return "ManifestationDays{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", manifestation=" + manifestation.getName() +
                ", tickets=" + tickets +
                ", manifestationSectors=" + manifestationSectors +
                '}';
    }
}
