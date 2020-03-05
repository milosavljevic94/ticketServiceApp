package com.ftn.services.address;

import com.ftn.constants.AddressConst;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Address;
import com.ftn.repository.AddressRepository;
import com.ftn.service.AddressService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

    @RunWith(MockitoJUnitRunner.class)
    public class AddressServiceUnitTest {

        //@Autowired
        @InjectMocks
        private AddressService addressService;

        //@MockBean
        @Mock
        private AddressRepository addressRepository;

        @Before
        public void setUp(){
            Address a = new Address();
            Address a1 = new Address();

            a.setId(AddressConst.ADDRESS_MOCK_ID);
            a1.setId(AddressConst.ADDRESS_MOCK_ID2);

            List<Address> addresses = new ArrayList<>();
            addresses.add(a);
            addresses.add(a1);

            when(addressRepository.findById(AddressConst.ADDRESS_MOCK_ID))
                    .thenReturn(Optional.of(a));
            when(addressRepository.findAll()).thenReturn(addresses);
        }


        @Test
        public void findAllAddressTest_thenReturnAddressList(){
            List<Address> list = addressService.finfAllAddress();
            assertEquals(2, list.size());
        }

        @Test
        public void findOneAddressExistTest_thenReturnAddress(){
            Long id = AddressConst.ADDRESS_MOCK_ID;
            Address a = addressService.findOneAddress(id);
            assertEquals(id, a.getId());
        }

        @Test(expected = EntityNotFoundException.class)
        public void findOneAddressNotExistTest_thenThrowException(){
            Long id = AddressConst.ADDRESS_WRONG_ID;
            Address a = addressService.findOneAddress(id);
        }

        @Test
        public void addAddressTest(){

            Address address = AddressConst.newAddressToAdd();
            when(addressRepository.save(any(Address.class))).thenAnswer(returnsFirstArg());
            Address addressTest = addressService.addAddress(address);

            assertNotNull(addressTest);
            assertEquals(address, addressTest);
            verify(addressRepository, times(1)).save(address);
        }

        @Test
        public void deleteAddressTest(){

            Address address = AddressConst.newAddressToDelete();

            addressService.deleteAddress(address.getId());

            verify(addressRepository, times(1)).deleteById(address.getId());
        }
    }
