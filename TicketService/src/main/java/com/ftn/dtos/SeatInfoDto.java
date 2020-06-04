package com.ftn.dtos;

public class SeatInfoDto {

    private Long manSectorId;
    private int row;
    private int seatNumber;
    private Boolean taken;

    public SeatInfoDto() {
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

    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }
}
