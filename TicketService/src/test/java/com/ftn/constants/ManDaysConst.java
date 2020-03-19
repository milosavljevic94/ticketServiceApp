package com.ftn.constants;

import com.ftn.dtos.ManifestationDaysDto;
import com.ftn.model.ManifestationDays;

import java.time.LocalDateTime;

public class ManDaysConst {

    public static Long VALID_ID = 1l;
    public static Long NOT_VALID_ID = 111L;
    public static int DAYS_SIZE = 5;

    public static String VALID_NAME = "Dan1 man1";
    public static String VALID_DESC = "Opis dana prvog za manifestaciju 1.";
    public static LocalDateTime VALID_TIME = LocalDateTime.of(2020,02,19, 20,00,00);

    public static ManifestationDaysDto newDtoToUpdate(){

        ManifestationDaysDto daysDtoUpdate = new ManifestationDaysDto();
        daysDtoUpdate.setId(VALID_ID);
        daysDtoUpdate.setName("novoIme");
        daysDtoUpdate.setDescription("noviOpis");

        return daysDtoUpdate;
    }

    public static ManifestationDays newManDay() {
        ManifestationDays manifestationDays = new ManifestationDays();
        manifestationDays.setName("Day for delete");

        return manifestationDays;
    }

    public ManDaysConst() {
    }


}
