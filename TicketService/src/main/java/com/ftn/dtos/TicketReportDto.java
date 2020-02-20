package com.ftn.dtos;

public class TicketReportDto {

    private double profit;

    private int soldTicketNumber;

    public TicketReportDto(){

    }

    public TicketReportDto(double profit, int soldTicketNumber) {
        this.profit = profit;
        this.soldTicketNumber = soldTicketNumber;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getSoldTicketNumber() {
        return soldTicketNumber;
    }

    public void setSoldTicketNumber(int soldTicketNumber) {
        this.soldTicketNumber = soldTicketNumber;
    }
}
