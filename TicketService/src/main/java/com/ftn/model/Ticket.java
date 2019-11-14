package com.ftn.model;

import javax.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rowNum;

    private Integer seatNum;

    @OneToOne(mappedBy = "ticket")
    private Reservation reservation;

    //To Do : relation to ManSec and ManDays.


    public Ticket() {
    }

    public Ticket(Integer rowNum, Integer seatNum, Reservation reservation) {
        this.rowNum = rowNum;
        this.seatNum = seatNum;
        this.reservation = reservation;
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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", rowNum=" + rowNum +
                ", seatNum=" + seatNum +
                ", reservation=" + reservation +
                '}';
    }
}
