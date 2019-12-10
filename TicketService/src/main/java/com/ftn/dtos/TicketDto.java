package com.ftn.dtos;

import com.ftn.model.Ticket;

public class TicketDto {

    private Long id;

    private Integer rowNum;

    private Integer seatNum;

    private ReservationDto reservation;

    public TicketDto(){}

    public TicketDto(Ticket ticket){

        this.id = ticket.getId();
        this.rowNum = ticket.getRowNum();
        this.seatNum = ticket.getSeatNum();
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

    public ReservationDto getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDto reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "TicketDto{" +
                "id=" + id +
                ", rowNum=" + rowNum +
                ", seatNum=" + seatNum +
                ", reservation=" + reservation +
                '}';
    }
}
