package com.ftn.controllers;

import com.ftn.constants.AddressConst;
import com.ftn.dtos.AddressDto;
import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.model.Address;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.AddressRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class AddressControllerTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    @Before
    public void login() {
        ResponseEntity<LoggedInUserDTO> login = restTemplate.postForEntity("/login",  new LoginDTO("testAdmin", "admin"),
                LoggedInUserDTO.class);

        accessToken = login.getBody().getToken();
    }

    private HttpEntity<Object> createRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        return new HttpEntity<>(headers);
    }


    @Test
    public void testGetAllAddresses_thenReturnAddressList() {
        ResponseEntity<AddressDto[]> response = restTemplate.exchange("/api/address/allAddress",HttpMethod.GET,
                createRequestEntity(), AddressDto[].class);
        AddressDto[] addressesDto = response.getBody();
        AddressDto a1 = addressesDto[0];


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(addressesDto);
        assertNotEquals(0, addressesDto.length);
        assertEquals(2, addressesDto.length);
        assertEquals(AddressConst.DB_STATE, a1.getState());
        assertEquals(AddressConst.DB_CITY, a1.getCity());
        assertEquals(AddressConst.DB_STREET, a1.getStreet());
        assertEquals(AddressConst.DB_NUM, a1.getNumber());
    }

    @Test
    public void testGetAddress_thenReturnAddress() {
        Address expected = addressRepository.findById(AddressConst.VALID_ID_ADDRESS).get();

        ResponseEntity<AddressDto> response = restTemplate.exchange("/api/address/"+AddressConst.VALID_ID_ADDRESS,
                HttpMethod.GET, createRequestEntity(), AddressDto.class);
        AddressDto found = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(found);
        assertEquals(expected.getId(), found.getId());
        assertEquals(expected.getState(), found.getState());
        assertEquals(expected.getStreet(), found.getStreet());
        assertEquals(expected.getCity(), found.getCity());
        assertEquals(expected.getNumber(), found.getNumber());
    }

    @Test
    public void testGetAddressNotValidId_thenReturnNotFound() {
        ResponseEntity<AddressDto> response = restTemplate.exchange("/api/address/" + AddressConst.NOT_VALID_ID_ADDRESS,
                HttpMethod.GET, createRequestEntity(), AddressDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddAddress_thenReturnAddedAddress(){
        int sizeBeforeAdd = addressRepository.findAll().size();

        AddressDto dtoToAdd = AddressConst.newAddressDtoToAdd();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<AddressDto> requestEntity = new HttpEntity<>(dtoToAdd, headers);

        ResponseEntity<AddressDto> response = restTemplate.exchange("/api/address/addAddress", HttpMethod.POST,
                requestEntity, AddressDto.class);

        AddressDto responseAddress = response.getBody();


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(responseAddress);

        int sizeAfterAdd = addressRepository.findAll().size();
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);

        List<Address> addresses = addressRepository.findAll();
        Address lastAddress = addresses.get(addresses.size() - 1);

        assertEquals(dtoToAdd.getState(), lastAddress.getState());
        assertEquals(dtoToAdd.getCity(), lastAddress.getCity());
        assertEquals(dtoToAdd.getStreet(), lastAddress.getStreet());
        assertEquals(dtoToAdd.getNumber(), lastAddress.getNumber());
        assertEquals(dtoToAdd.getLongitude(), lastAddress.getLongitude());
        assertEquals(dtoToAdd.getLatitude(), lastAddress.getLatitude());

        //Delete added address from db.

        addressRepository.delete(lastAddress);
    }

    @Test
    public void testUpdateAddress_thenReturnUpdated(){

        AddressDto dtoToUpdate = AddressConst.newAddressDtoForUpdate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<AddressDto> requestEntity = new HttpEntity<>(dtoToUpdate, headers);

        ResponseEntity<AddressDto> response = restTemplate.exchange("/api/address/updateAddress", HttpMethod.PUT,
                requestEntity, AddressDto.class);

        AddressDto responseAddress = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseAddress);

        Address updated = addressRepository.findById(AddressConst.VALID_ID_ADDRESS).get();

        assertEquals(dtoToUpdate.getState(), updated.getState());
        assertEquals(dtoToUpdate.getCity(), updated.getCity());
        assertEquals(dtoToUpdate.getStreet(), updated.getStreet());
        assertEquals(dtoToUpdate.getNumber(), updated.getNumber());
        assertEquals(dtoToUpdate.getLongitude(), updated.getLongitude());
        assertEquals(dtoToUpdate.getLatitude(), updated.getLatitude());
    }

    @Test
    public void testUpdateAddressNotValidId_thenNotFound(){

        AddressDto dtoToUpdate = AddressConst.newAddressDtoForUpdate();
        dtoToUpdate.setId(AddressConst.NOT_VALID_ID_ADDRESS);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<AddressDto> requestEntity = new HttpEntity<>(dtoToUpdate, headers);

        ResponseEntity<AddressDto> response = restTemplate.exchange("/api/address/updateAddress", HttpMethod.PUT,
                requestEntity, AddressDto.class);

        AddressDto responseAddress = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteAddress(){

        addressRepository.save(AddressConst.newAddressToDelete());

        int sizeBeforeDel = addressRepository.findAll().size();

        ResponseEntity<?> response = restTemplate.exchange("/api/address/"+AddressConst.ID_ADDRESS_FOR_DELETE, HttpMethod.DELETE,
                createRequestEntity(), String.class);

        int sizeAfterDel = addressRepository.findAll().size();
        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }
}
