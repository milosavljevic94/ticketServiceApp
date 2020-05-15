package com.ftn.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rowNum;

    private Integer seatNum;

    private Boolean purchaseConfirmed;

    private LocalDateTime purchaseTime;

    @OneToOne(mappedBy = "ticket", orphanRemoval = true)
    private Reservation reservation;

    //Relation to Manifestation Sector and Manifestation Days.

    @ManyToOne
    @JoinColumn(name = "manifestationSector_id", referencedColumnName = "id")
    private ManifestationSector manifestationSector;

    @ManyToOne
    @JoinColumn(name = "manifestationDays_id", referencedColumnName = "id")
    private ManifestationDays manifestationDays;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Ticket() {
    }

    public Ticket(Integer rowNum, Integer seatNum, Boolean purchaseConfirmed, LocalDateTime purchaseTime, Reservation reservation,
                  ManifestationSector manifestationSector, ManifestationDays manifestationDays,
                  User user) {
        this.rowNum = rowNum;
        this.seatNum = seatNum;
        this.purchaseConfirmed = purchaseConfirmed;
        this.purchaseTime = purchaseTime;
        this.reservation = reservation;
        this.manifestationSector = manifestationSector;
        this.manifestationDays = manifestationDays;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    public Boolean getPurchaseConfirmed() {
        return purchaseConfirmed;
    }

    public void setPurchaseConfirmed(Boolean purchaseConfirmed) {
        this.purchaseConfirmed = purchaseConfirmed;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public ManifestationSector getManifestationSector() {
        return manifestationSector;
    }

    public void setManifestationSector(ManifestationSector manifestationSector) {
        this.manifestationSector = manifestationSector;
    }

    public ManifestationDays getManifestationDays() {
        return manifestationDays;
    }

    public void setManifestationDays(ManifestationDays manifestationDays) {
        this.manifestationDays = manifestationDays;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", rowNum=" + rowNum +
                ", seatNum=" + seatNum +
                ", purchaseConfirmed=" + purchaseConfirmed +
                ", purchaseTime=" + purchaseTime +
                ", reservation=" + reservation +
                ", manifestationSector=" + manifestationSector +
                ", manifestationDays=" + manifestationDays.getName() +
                ", user=" + user.getUsername() +
                '}';
    }
}
