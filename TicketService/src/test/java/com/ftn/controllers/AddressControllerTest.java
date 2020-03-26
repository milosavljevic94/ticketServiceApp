package com.ftn.controllers;

import com.ftn.constants.AddressConst;
import com.ftn.dtos.AddressDto;
import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
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

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
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
    public void testGetAllAddresses() {
        ResponseEntity<AddressDto[]> responseEntity = restTemplate.exchange("/api/address/allAddress",HttpMethod.GET, createRequestEntity(), AddressDto[].class);
        AddressDto[] addressesDto = responseEntity.getBody();
        AddressDto a1 = addressesDto[0];


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(addressesDto);
        assertNotEquals(0, addressesDto.length);
        assertEquals(2, addressesDto.length);
        assertEquals(AddressConst.DB_STATE, a1.getState());
        assertEquals(AddressConst.DB_CITY, a1.getCity());
        assertEquals(AddressConst.DB_STREET, a1.getStreet());
        assertEquals(AddressConst.DB_NUM, a1.getNumber());
    }

    @Test
    public void testGetAddress() {
        ResponseEntity<AddressDto> responseEntity = restTemplate.exchange("/api/address/"+AddressConst.VALID_ID_ADDRESS, HttpMethod.GET, createRequestEntity(), AddressDto.class);
        AddressDto found = responseEntity.getBody();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(found);
        assertEquals(AddressConst.VALID_ID_ADDRESS, found.getId());
        assertEquals(AddressConst.DB_STREET, found.getStreet());
    }
}
