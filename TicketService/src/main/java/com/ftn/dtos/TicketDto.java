package com.ftn.dtos;

import com.ftn.model.Ticket;

import java.time.LocalDateTime;

public class TicketDto {

    private Long id;

    private Integer rowNum;

    private Integer seatNum;

    private Boolean purchaseConfirmed;

    private LocalDateTime purchaseTime;

    private ReservationDto reservation;

    public TicketDto(){}

    public TicketDto(Ticket ticket){

        this.id = ticket.getId();
        this.rowNum = ticket.getRowNum();
        this.seatNum = ticket.getSeatNum();
        this.purchaseConfirmed = ticket.getPurchaseConfirmed();
        this.purchaseTime = ticket.getPurchaseTime();
        if(ticket.getReservation() != null) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(ticket.getReservation().getId());
            reservationDto.setExpDays(ticket.getReservation().getExpDays());
            reservationDto.setActive(ticket.getReservation().getActive());
            UserDto userDto = new UserDto();
            userDto.setId(ticket.getReservation().getUser().getId());
            userDto.setFirstName(ticket.getReservation().getUser().getFirstName());
            userDto.setLastName(ticket.getReservation().getUser().getLastName());
            userDto.setActive(ticket.getReservation().getUser().getActive());
            reservationDto.setUser(userDto);
            this.reservation = reservationDto;
        }else{
            this.reservation = null;
        }

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

    public ReservationDto getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDto reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "TicketDto{" +
                "rowNum=" + rowNum +
                ", seatNum=" + seatNum +
                ", purchaseConfirmed=" + purchaseConfirmed +
                ", purchaseTime=" + purchaseTime +
                '}';
    }
}
