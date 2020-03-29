package com.ftn.controllers;

import com.ftn.constants.AddressConst;
import com.ftn.constants.LocationConst;
import com.ftn.dtos.LocationDto;
import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.AddressRepository;
import com.ftn.repository.LocationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class LocationControllerTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AddressRepository addressRepository;

    private String accessToken;

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void testGetAllLocations_thenReturnLocationList() {
        ResponseEntity<LocationDto[]> response = restTemplate.exchange("/api/location/allLocation", HttpMethod.GET,
                createRequestEntity(), LocationDto[].class);
        LocationDto[] locationDto = response.getBody();
        LocationDto l1 = locationDto[0];


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(locationDto);
        assertNotEquals(0, locationDto.length);
        assertEquals(2, locationDto.length);
        assertEquals(LocationConst.VALID_LOCATION_NAME, l1.getLocationName());
        assertEquals(AddressConst.DB_CITY, l1.getAddress().getCity());
        assertEquals(AddressConst.DB_STREET, l1.getAddress().getStreet());
        assertEquals(AddressConst.DB_NUM, l1.getAddress().getNumber());
    }
}
