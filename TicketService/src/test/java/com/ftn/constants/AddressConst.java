package com.ftn.constants;

import com.ftn.dtos.AddressDto;
import com.ftn.model.Address;

public class AddressConst {

    //for unit testing
    public static final Long ADDRESS_MOCK_ID = 123L;
    public static final Long ADDRESS_MOCK_ID2 = 1234L;

    public static final Long ADDRESS_WRONG_ID = 6563L;

    //for update address
    public static final String ADDRESS_STATE = "Serbia";
    public static final String ADDRESS_CITY = "Bor";

    public static Address newAddressToAdd(){
        Address address = new Address();

        address.setLongitude(12.33);
        address.setLatitude(14.22);
        address.setNumber("12");
        address.setStreet("Nova ulica za test");
        address.setCity("Grad test");
        address.setState("Drzava test");

        return address;
    }

    public static Address newAddressToDelete(){
        Address address = new Address();

        address.setId(ADDRESS_MOCK_ID);

        return address;
    }

    public static AddressDto newAddressDto(){

        AddressDto addressDto = new AddressDto();

        return addressDto;
    }

    //for integration testing

    public static String DB_STATE = "Srbija";
    public static String DB_CITY = "Beograd";
    public static String DB_STREET = "Ljutice Bogdana";
    public static String DB_NUM = "1a";

    public static Long VALID_ID_ADDRESS = 1L;
    public static Long NOT_VALID_ID_ADDRESS = 111L;
    public static Long ID_ADDRESS_FOR_DELETE = 3L;


    public static AddressDto newAddressDtoToAdd(){
        AddressDto address = new AddressDto();

        address.setLongitude(12.33);
        address.setLatitude(14.22);
        address.setNumber("12");
        address.setStreet("Ulica add test");
        address.setCity("Grad add test");
        address.setState("Drzava add test");

        return address;
    }

    public static AddressDto newAddressDtoForUpdate(){

        AddressDto addressDto = new AddressDto();
        addressDto.setId(VALID_ID_ADDRESS);
        addressDto.setCity("testCity");
        addressDto.setState("testState");
        addressDto.setStreet("testStreet");
        addressDto.setNumber("testNumber");

        return addressDto;
    }

    public AddressConst() {
    }
}
