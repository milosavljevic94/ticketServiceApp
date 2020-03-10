package com.ftn.constants;

import com.ftn.dtos.ManifestationDaysDto;
import com.ftn.dtos.ManifestationDto;
import com.ftn.dtos.ManifestationSectorPriceDto;
import com.ftn.enums.ManifestationCategory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ManifestationConst {


    public static int MAN1_SECTOR_NUM = 4;
    public static int MAN1_DAYS_NUM = 3;

    public static Long LOCATION_ID = 1L;
    public static Long WRONG_LOCATION_ID = 88L;

    public static Long OK_MAN_ID = 1L;
    public static Long BAD_MAN_ID = 144L;


    public static ManifestationDto newDto(){

        ManifestationDaysDto testDay = new ManifestationDaysDto();
        testDay.setId(11L);
        testDay.setName("testdan");
        testDay.setDescription("testDesc");
        testDay.setStartTime(LocalDateTime.of(2020, 03, 01, 12, 12, 00));
        testDay.setManifestationId(OK_MAN_ID);
        Set<ManifestationDaysDto> days = new HashSet<>();
        days.add(testDay);


        ManifestationDto manifestationDto = new ManifestationDto();
        manifestationDto.setName("testMan");
        manifestationDto.setDescription("testDesc");
        manifestationDto.setStartTime(LocalDateTime.of(2020, 03, 01, 12, 12, 00));
        manifestationDto.setEndTime(LocalDateTime.of(2020, 03, 02, 12, 12, 00));
        manifestationDto.setManifestationCategory(ManifestationCategory.CONCERT);
        manifestationDto.setManDaysDto(days);
        manifestationDto.setLocationId(LOCATION_ID);

        return manifestationDto;
    }

    //wrong date.
    public static ManifestationDto newDtoWrongDate(){

        ManifestationDto manifestationDto = new ManifestationDto();
        manifestationDto.setStartTime(LocalDateTime.of(2020, 03, 05, 12, 12, 00));
        manifestationDto.setEndTime(LocalDateTime.of(2020, 03, 01, 12, 12, 00));

        return manifestationDto;
    }

    //wrong location.
    public static ManifestationDto newDtoWrongLocation(){

        ManifestationDto manifestationDto = new ManifestationDto();
        manifestationDto.setStartTime(LocalDateTime.of(2020, 03, 01, 12, 12, 00));
        manifestationDto.setEndTime(LocalDateTime.of(2020, 03, 05, 12, 12, 00));
        manifestationDto.setLocationId(WRONG_LOCATION_ID);

        return manifestationDto;
    }

    //wrong days number.
    public static ManifestationDto newDtoWrongDaysNumber(){

        ManifestationDaysDto testDay = new ManifestationDaysDto();
        testDay.setId(11L);
        testDay.setName("testdan");
        testDay.setDescription("testDesc");
        testDay.setStartTime(LocalDateTime.of(2020, 03, 01, 12, 12, 00));
        testDay.setManifestationId(OK_MAN_ID);
        Set<ManifestationDaysDto> days = new HashSet<>();
        days.add(testDay);


        ManifestationDto manifestationDto = new ManifestationDto();
        manifestationDto.setStartTime(LocalDateTime.of(2020, 03, 01, 12, 12, 00));
        manifestationDto.setEndTime(LocalDateTime.of(2020, 03, 05, 12, 12, 00));
        manifestationDto.setLocationId(LOCATION_ID);
        manifestationDto.setManDaysDto(days);

        return manifestationDto;
    }

    public static ManifestationDto newDtoForUpdateWrongId(){

        ManifestationDto manifestationDto = new ManifestationDto();
        manifestationDto.setId(ManifestationConst.BAD_MAN_ID);

        return manifestationDto;
    }

    public static ManifestationDto newDtoForUpdateWrongDate(){

        ManifestationDto manifestationDto = new ManifestationDto();
        manifestationDto.setId(ManifestationConst.OK_MAN_ID);
        manifestationDto.setName("dtoUpdateTest");
        manifestationDto.setDescription("descTest");
        manifestationDto.setManifestationCategory(ManifestationCategory.SPORTS);
        manifestationDto.setStartTime(LocalDateTime.of(2020, 03, 03, 12, 12, 00));
        manifestationDto.setEndTime(LocalDateTime.of(2020, 03, 01, 12, 12, 00));

        return manifestationDto;
    }

    public static ManifestationDto newDtoForUpdate(){

        ManifestationDto manifestationDto = new ManifestationDto();
        manifestationDto.setId(ManifestationConst.OK_MAN_ID);
        manifestationDto.setName("dtoUpdateTest");
        manifestationDto.setDescription("descTest");
        manifestationDto.setManifestationCategory(ManifestationCategory.SPORTS);
        manifestationDto.setStartTime(LocalDateTime.of(2020, 03, 01, 12, 12, 00));
        manifestationDto.setEndTime(LocalDateTime.of(2020, 03, 02, 12, 12, 00));

        return manifestationDto;
    }

    public static ManifestationSectorPriceDto newSectorPrice(){
        ManifestationSectorPriceDto dto = new ManifestationSectorPriceDto();
        dto.setDayId(1L);
        dto.setSectorId(1L);
        dto.setPrice(200.00);

        return dto;
    }

    public ManifestationConst() {
    }
}
