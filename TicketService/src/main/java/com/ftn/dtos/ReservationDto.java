package com.ftn.dtos;

import com.ftn.model.Reservation;

public class ReservationDto {

    private Long id;

    private UserDto user;

    private Integer expDays;

    private Boolean active;

    private BuyTicketDto ticket;

    public ReservationDto(){ }

    public ReservationDto(Reservation reservation){

        this.id = reservation.getId();

        UserDto udto = new UserDto();
            udto.setUserName(reservation.getUser().getUsername());
            udto.setFirstName(reservation.getUser().getFirstName());
            udto.setLastName(reservation.getUser().getLastName());
            udto.setActive(reservation.getUser().getActive());
        this.user = udto;

        this.expDays = reservation.getExpDays();
        this.active = reservation.getActive();

        BuyTicketDto ticketDto = new BuyTicketDto();
        ticketDto.setDayId(reservation.getTicket().getManifestationDays().getId());
        ticketDto.setTicketId(reservation.getTicket().getId());
        ticketDto.setPurchaseConfirmed(reservation.getTicket().getPurchaseConfirmed());
        ticketDto.setPurchaseTime(reservation.getTicket().getPurchaseTime());
        
        SeatWithPriceDto wantedSeat = new SeatWithPriceDto();
        wantedSeat.setRow(reservation.getTicket().getRowNum());
        wantedSeat.setSeatNumber(reservation.getTicket().getSeatNum());
        wantedSeat.setManSectorId(reservation.getTicket().getManifestationSector().getId());
        ticketDto.setWantedSeat(wantedSeat);
        
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

    public BuyTicketDto getTicket() {
        return ticket;
    }

    public void setTicket(BuyTicketDto ticket) {
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
