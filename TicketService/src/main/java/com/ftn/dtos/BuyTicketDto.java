package com.ftn.dtos;

import java.time.LocalDateTime;

import com.ftn.model.Ticket;

public class BuyTicketDto {

    private Long dayId;
    private Long ticketId;
    private SeatWithPriceDto wantedSeat = new SeatWithPriceDto();
    private Boolean purchaseConfirmed;
    private LocalDateTime purchaseTime;
    
    public BuyTicketDto(Ticket ticket){

        this.dayId = ticket.getManifestationDays().getId();
        this.wantedSeat.setManSectorId(ticket.getManifestationSector().getId());
        this.wantedSeat.setRow(ticket.getRowNum());
        this.wantedSeat.setSeatNumber(ticket.getSeatNum());
        this.purchaseConfirmed = ticket.getPurchaseConfirmed();
        this.purchaseTime = ticket.getPurchaseTime();
        this.ticketId = ticket.getId();
    }
    
    public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
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

	public BuyTicketDto() {
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public SeatWithPriceDto getWantedSeat() {
        return wantedSeat;
    }

    public void setWantedSeat(SeatWithPriceDto wantedSeat) {
        this.wantedSeat = wantedSeat;
    }
}
