package com.ftn.model;

import javax.persistence.*;

@Entity
@Table(name = "manifestation_sector")
public class ManifestationSector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manifestation_days_id")
    private ManifestationDays manifestationDays;

    @OneToOne
    @JoinColumn(name = "sector_id", referencedColumnName = "id", nullable = true)
    private Sector sector;

    private Double price;

    public ManifestationSector() {
    }

    public ManifestationSector(Long id,ManifestationDays manifestationDays, Sector sector,
                               Double price){
        this.id = id;
        this.manifestationDays = manifestationDays;
        this.sector = sector;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ManifestationDays getManifestationDays() {
        return manifestationDays;
    }

    public void setManifestationDays(ManifestationDays manifestationDays) {
        this.manifestationDays = manifestationDays;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ManifestationSector{" +
                "id=" + id +
                ", manifestationDays=" + manifestationDays.getName() +
                ", sector=" + sector +
                ", price=" + price +
                '}';
    }
}
