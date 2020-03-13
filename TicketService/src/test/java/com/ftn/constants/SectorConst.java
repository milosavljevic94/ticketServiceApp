package com.ftn.constants;

import com.ftn.dtos.SectorDto;

public class SectorConst {

    //integration testing.
    public static int VALID_SIZE_SECTOR = 7;
    public static String VALID_NAME = "sectorSever";
    public static Integer VALID_ROWS = 22;
    public static Integer VALID_COLUMNS = 12;
    public static Integer SEATS_NUM = VALID_COLUMNS*VALID_ROWS;

    public static Long ID_TO_DELETE = 8L;

    //unit testing.
    public static Long OK_ID_SECTOR = 1L;
    public static Long BAD_ID_SECTOR = 3232L;

    public static SectorDto newDtoToAdd(){

        SectorDto sectorDto = new SectorDto();
        sectorDto.setSectorName("noviSektorTest");
        sectorDto.setColumns(3);
        sectorDto.setRows(11);
        sectorDto.setSeatsNumber(33);
        sectorDto.setLocationId(1L);

        return sectorDto;
    }


    public SectorConst() {
    }
}
