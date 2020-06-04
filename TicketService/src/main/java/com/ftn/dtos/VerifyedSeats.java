package com.ftn.dtos;

import java.util.ArrayList;
import java.util.List;

public class VerifyedSeats {

    private int rowNum;

    private int columnNum;

    private List<SeatInfoDto> takenSeats = new ArrayList<>();

    public VerifyedSeats() {
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public List<SeatInfoDto> getTakenSeats() {
        return takenSeats;
    }

    public void setTakenSeats(List<SeatInfoDto> takenSeats) {
        this.takenSeats = takenSeats;
    }
}
