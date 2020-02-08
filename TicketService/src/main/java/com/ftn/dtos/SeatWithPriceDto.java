package com.ftn.dtos;

public class SeatWithPriceDto {

    private Long manSectorId;
    private int row;
    private int seatNumber;

    public SeatWithPriceDto() {
    }

    public Long getManSectorId() {
        return manSectorId;
    }

    public void setManSectorId(Long manSectorId) {
        this.manSectorId = manSectorId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
