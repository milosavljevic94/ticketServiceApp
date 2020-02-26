package com.ftn.constants;

import com.ftn.dtos.AddressDto;
import com.ftn.dtos.LocationDto;
import com.ftn.dtos.SectorDto;
import com.ftn.model.Location;

public class LocationConst {

    //exist in db
    public static final String REAL_LOCATION_NAME = "Spens";

    //not exist in db
    public static final String BAD_LOCATION_NAME = "NijeSpens";

    //for unit testing
    public static final Long REAL_LOCATION_ID = 123L;
    public static final String LOCATION_MOCK_NAME = "locationTest1";
    public static final Long LOCATION_MOCK_ID2 = 1234L;
    public static final String LOCATION_MOCK_NAME2 = "locationTest2";

    public static final Long LOCATION_WRONG_ID = 6563L;

    public static final String LOCATION_UPDATE_NAME = "Name uprade test";

    public static LocationDto newLocationDto(){

        LocationDto locationDto = new LocationDto();

        return locationDto;
    }


    public static Location newLocation(){

        Location location = new Location();
        location.setLocationName(LocationConst.LOCATION_MOCK_NAME);

        return location;
    }

    //for test add method

    public static AddressDto newAddressDtoToAdd(){

        AddressDto addressDto = new AddressDto();
        addressDto.setId(12L);
        addressDto.setLongitude(12.33);
        addressDto.setLatitude(44.44);
        addressDto.setNumber("3232");
        addressDto.setCity("testgrad");
        addressDto.setStreet("testulica");
        addressDto.setState("testdrzava");

        return addressDto;
    }

    public static LocationDto newLocationDtoToAdd(){

        LocationDto locationDto = new LocationDto();
        locationDto.setLocationName("testLokacijaDodavanje");
        locationDto.setAddress(LocationConst.newAddressDtoToAdd());
        return locationDto;
    }


    //for add sector to location
    public static SectorDto newSectorDtoTest(){

        SectorDto sectorDto = new SectorDto();

        return sectorDto;
    }

    public LocationConst() {
    }
}
