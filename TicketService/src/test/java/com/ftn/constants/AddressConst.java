package com.ftn.constants;

import com.ftn.model.Address;

public class AddressConst {

    //for unit testing
    public static final Long ADDRESS_MOCK_ID = 123L;
    public static final Long ADDRESS_MOCK_ID2 = 1234L;

    public static final Long ADDRESS_WRONG_ID = 6563L;

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

    public AddressConst() {
    }
}
