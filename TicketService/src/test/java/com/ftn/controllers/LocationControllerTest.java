package com.ftn.controllers;

import com.ftn.constants.AddressConst;
import com.ftn.constants.LocationConst;
import com.ftn.constants.ManifestationConst;
import com.ftn.dtos.LocationDto;
import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.dtos.ManifestationInfoDto;
import com.ftn.model.Address;
import com.ftn.model.Location;
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

import java.util.List;

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

    @Test
    public void testGetLocation_thenReturnLocationDto() {
        ResponseEntity<LocationDto> response = restTemplate.exchange("/api/location/"+LocationConst.VALID_LOC_ID, HttpMethod.GET,
                createRequestEntity(), LocationDto.class);
        LocationDto locationDto = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(locationDto);
        assertEquals(LocationConst.VALID_LOCATION_NAME, locationDto.getLocationName());
        assertNotNull(locationDto.getAddress());
        assertNotNull(locationDto.getSectors());
    }

    @Test
    public void testGetLocationNotValidId_thenReturnNotFound() {
        ResponseEntity<LocationDto> response = restTemplate.exchange("/api/location/" + LocationConst.NOT_VALID_LOC_ID,
                HttpMethod.GET, createRequestEntity(), LocationDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetManifestationsOfLocation_thenReturnManifestationList() {
        ResponseEntity<ManifestationInfoDto[]> response = restTemplate.exchange("/api/location/getLocationManifestations/"+LocationConst.VALID_LOC_ID,
                HttpMethod.GET,
                createRequestEntity(), ManifestationInfoDto[].class);
        ManifestationInfoDto[] manInfoDtos = response.getBody();
        ManifestationInfoDto resultManInfo = manInfoDtos[0];


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(manInfoDtos);
        assertNotEquals(0, manInfoDtos.length);
        assertEquals(1, manInfoDtos.length);
        assertEquals(ManifestationConst.VALID_NAME, resultManInfo.getManifestationName());
    }

    @Test
    public void testAddLocationAndAddressExistingName_thenConflict(){
        LocationDto dtoToAdd = LocationConst.newDtoToAdd();
        dtoToAdd.setLocationName(LocationConst.EXITS_NAME);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<LocationDto> requestEntity = new HttpEntity<>(dtoToAdd, headers);

        ResponseEntity<LocationDto> response = restTemplate.exchange("/api/location/addLocation", HttpMethod.POST,
                requestEntity, LocationDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }

    @Test
    public void testAddLocationAndAddress_thenReturnAddedLocation(){
        int sizeBeforeAdd = locationRepository.findAll().size();

        LocationDto dtoToAdd = LocationConst.newDtoToAdd();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<LocationDto> requestEntity = new HttpEntity<>(dtoToAdd, headers);

        ResponseEntity<LocationDto> response = restTemplate.exchange("/api/location/addLocation", HttpMethod.POST,
                requestEntity, LocationDto.class);

        LocationDto resLocation = response.getBody();


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(resLocation);

        int sizeAfterAdd = locationRepository.findAll().size();
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);

        List<Location> locations = locationRepository.findAll();
        Location lastLocation = locations.get(locations.size() - 1);

        assertEquals(dtoToAdd.getLocationName(), lastLocation.getLocationName());
        assertEquals(dtoToAdd.getAddress().getState(), lastLocation.getAddress().getState());
        assertEquals(dtoToAdd.getAddress().getCity(), lastLocation.getAddress().getCity());
        assertEquals(dtoToAdd.getAddress().getStreet(), lastLocation.getAddress().getStreet());
        assertEquals(dtoToAdd.getAddress().getNumber(), lastLocation.getAddress().getNumber());
        assertEquals(dtoToAdd.getAddress().getLongitude(), lastLocation.getAddress().getLongitude());
        assertEquals(dtoToAdd.getAddress().getLatitude(), lastLocation.getAddress().getLatitude());

        //Delete added location from db.
        List<Address> addresses = addressRepository.findAll();
        Address lastAddress = addresses.get(addresses.size() - 1);

        locationRepository.delete(lastLocation);
        addressRepository.delete(lastAddress);
    }


    @Test
    public void testUpdateLocation_thenReturnUpdated(){

        LocationDto dtoToUpdate = LocationConst.newDtoToUpdate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<LocationDto> requestEntity = new HttpEntity<>(dtoToUpdate, headers);

        ResponseEntity<LocationDto> response = restTemplate.exchange("/api/location/updateLocation", HttpMethod.PUT,
                requestEntity, LocationDto.class);

        LocationDto responseLocation = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseLocation);

        Location updated = locationRepository.findById(LocationConst.VALID_LOC_ID).get();

        assertEquals(dtoToUpdate.getLocationName(), updated.getLocationName());
    }

    @Test
    public void testDeleteLocation(){

        locationRepository.save(LocationConst.newLocationToDelete());

        int sizeBeforeDel = locationRepository.findAll().size();

        ResponseEntity<?> response = restTemplate.exchange("/api/location/"+LocationConst.ID_ADDRESS_FOR_DELETE, HttpMethod.DELETE,
                createRequestEntity(), String.class);

        int sizeAfterDel = locationRepository.findAll().size();
        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }
}
