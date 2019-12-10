package com.ftn.dtos;

import com.ftn.model.Reservation;

public class ReservationDto {

    private Long id;

    private UserDto user;

    private Integer expDays;

    private Boolean active;

    private TicketDto ticket;

    public ReservationDto(){ }

    public ReservationDto(Reservation reservation){

        this.id = reservation.getId();

        UserDto udto = new UserDto();
            udto.setId(reservation.getUser().getId());
            udto.setFirstName(reservation.getUser().getFirstName());
            udto.setLastName(reservation.getUser().getLastName());
            udto.setActive(reservation.getUser().getActive());
        this.user = udto;

        this.expDays = reservation.getExpDays();
        this.active = reservation.getActive();

        TicketDto ticketDto = new TicketDto();
            ticketDto.setId(reservation.getTicket().getId());
            ticketDto.setRowNum(reservation.getTicket().getRowNum());
            ticketDto.setSeatNum(reservation.getTicket().getSeatNum());
        this.ticket = ticketDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Integer getExpDays() {
        return expDays;
    }

    public void setExpDays(Integer expDays) {
        this.expDays = expDays;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public TicketDto getTicket() {
        return ticket;
    }

    public void setTicket(TicketDto ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "ReservationDto{" +
                "id=" + id +
                ", user=" + user +
                ", expDays=" + expDays +
                ", active=" + active +
                ", ticket=" + ticket +
                '}';
    }
}
