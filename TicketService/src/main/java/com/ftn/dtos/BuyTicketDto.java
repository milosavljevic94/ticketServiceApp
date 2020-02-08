package com.ftn.dtos;

public class BuyTicketDto {

    private Long dayId;
    private SeatWithPriceDto wantedSeat;

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
