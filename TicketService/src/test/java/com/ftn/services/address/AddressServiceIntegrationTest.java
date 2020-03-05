package com.ftn.services.address;

import com.ftn.constants.AddressConst;
import com.ftn.dtos.AddressDto;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Address;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.AddressRepository;
import com.ftn.service.AddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class AddressServiceIntegrationTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void findAllSuccessTest(){
        List<Address> result = addressService.finfAllAddress();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void findOneAddressExistTest_thenReturnAddress(){
        Long id = AddressConst.VALID_ID_ADDRESS;
        Address a = addressService.findOneAddress(id);
        assertEquals(id, a.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneAddressNotExistTest_thenThrowException(){
        Long id = AddressConst.NOT_VALID_ID_ADDRESS;
        Address a = addressService.findOneAddress(id);
    }

    @Test
    @Transactional @Rollback(true)
    public void addAddressSuccessTest(){

        int sizeBeforeAdd = addressRepository.findAll().size();

        Address address = AddressConst.newAddressToAdd();
        Address result = addressService.addAddress(address);

        int sizeAfterAdd  = addressRepository.findAll().size();

        assertNotNull(result);
        assertEquals(sizeBeforeAdd+1, sizeAfterAdd);
        assertEquals(address.getCity(), result.getCity());
        assertEquals(address.getState(), result.getState());
        assertEquals(address.getNumber(), result.getNumber());
        assertEquals(address.getLatitude(), result.getLatitude());
        assertEquals(address.getLongitude(), result.getLongitude());
        assertEquals(address.getStreet(), result.getStreet());
    }

    @Test
    @Transactional @Rollback(true)
    public void deleteAddressSuccessTest(){

        int sizeBeforeDelete = addressRepository.findAll().size();
        addressService.deleteAddress(AddressConst.VALID_ID_ADDRESS);
        int sizeAfterDelete = addressRepository.findAll().size();

        assertEquals(sizeBeforeDelete-1, sizeAfterDelete);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateAddressNotExistIdTest_thenThrowException(){
        AddressDto addressDto = AddressConst.newAddressDtoForUpdate();
        addressDto.setId(AddressConst.NOT_VALID_ID_ADDRESS);

        Address result = addressService.updateAddress(addressDto);
    }

    @Test
    @Transactional @Rollback(true)
    public void updateAddressSuccessTest(){
        AddressDto addressDto = AddressConst.newAddressDtoForUpdate();

        Address result = addressService.updateAddress(addressDto);

        assertEquals(addressDto.getState(), result.getState());
        assertEquals(addressDto.getCity(), result.getCity());
        assertEquals(addressDto.getStreet(), result.getStreet());
        assertEquals(addressDto.getNumber(), result.getNumber());
    }

}
