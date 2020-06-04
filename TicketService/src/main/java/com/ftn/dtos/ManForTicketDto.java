package com.ftn.dtos;

public class ManForTicketDto {

    String manName;

    public ManForTicketDto(){}

    public ManForTicketDto(String manName) {
        this.manName = manName;
    }

    public String getManName() {
        return manName;
    }

    public void setManName(String manName) {
        this.manName = manName;
    }
}
