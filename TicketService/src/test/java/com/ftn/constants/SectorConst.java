package com.ftn.constants;

import com.ftn.dtos.SectorDto;

public class SectorConst {

    public static Long OK_ID_SECTOR = 1L;
    public static Long BAD_ID_SECTOR = 3232L;

    public static SectorDto newDtoToAdd(){
        SectorDto sectorDto = new SectorDto();
        sectorDto.setSectorName("noviSektorTest");
        sectorDto.setColumns(3);
        sectorDto.setRows(11);
        sectorDto.setSeatsNumber(33);

        return sectorDto;
    }


    public SectorConst() {
    }
}
