package com.ftn.controllers;

import com.ftn.constants.LocationConst;
import com.ftn.constants.ManDaysConst;
import com.ftn.constants.ManifestationConst;
import com.ftn.dtos.*;
import com.ftn.enums.ManifestationCategory;
import com.ftn.model.Manifestation;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ManifestationRepository;
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
@TestPropertySource("classpath:application-test.properties")
public class ManifestationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ManifestationRepository repository;

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
    public void testGetAllManifestations_thenReturnManifestationsLisst(){

        ResponseEntity<ManifestationDto[]> response = restTemplate.exchange("/api/manifestation/allManifestations", HttpMethod.GET,
                createRequestEntity(), ManifestationDto[].class);
        ManifestationDto[] manifestationDtos = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(manifestationDtos);
        assertNotEquals(0, manifestationDtos.length);
        assertEquals(2, manifestationDtos.length);
    }

    @Test
    @Transactional  //keep open session
    public void testGetManifestation_thenReturnManifestation() {
        Manifestation expected = repository.findById(ManifestationConst.OK_MAN_ID).get();

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/"+ManifestationConst.OK_MAN_ID,
                HttpMethod.GET, createRequestEntity(), ManifestationDto.class);
        ManifestationDto found = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(found);
        assertEquals(expected.getName(), found.getName());
        assertEquals(expected.getDescription(), found.getDescription());
        assertEquals(expected.getStartTime(), found.getStartTime());
        assertEquals(expected.getEndTime(), found.getEndTime());
        assertEquals(expected.getManifestationCategory(), found.getManifestationCategory());
        assertEquals(expected.getManifestationDays().size(), found.getManDaysDto().size());
    }

    @Test
    public void testGetManifestationNotValidId_thenReturnNotFound() {
        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/" + ManifestationConst.BAD_MAN_ID,
                HttpMethod.GET, createRequestEntity(), ManifestationDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetManifestationDay_thenReturnManDay() {

        ResponseEntity<ManifestationDayDto> response = restTemplate.exchange("/api/manifestation/manifestationDay/"+ ManDaysConst.VALID_ID,
                HttpMethod.GET, createRequestEntity(), ManifestationDayDto.class);
        ManifestationDayDto found = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(found);
        assertEquals(ManDaysConst.VALID_NAME, found.getName());
        assertEquals(ManDaysConst.VALID_TIME, found.getStartTime());
        assertEquals(ManifestationConst.VALID_NAME, found.getManifestation().getName());
    }

    @Test
    public void testGetManifestationDayNotValidId_thenReturnNotFoun() {

        ResponseEntity<ManifestationDayDto> response = restTemplate.exchange("/api/manifestation/manifestationDay/"+ ManDaysConst.NOT_VALID_ID,
                HttpMethod.GET, createRequestEntity(), ManifestationDayDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetManifestationInfo_thenReturnInfoDto() {

        ResponseEntity<ManifestationInfoDto> response = restTemplate.exchange("/api/manifestation/manifestationInfo/"+ ManifestationConst.OK_MAN_ID,
                HttpMethod.GET, createRequestEntity(), ManifestationInfoDto.class);
        ManifestationInfoDto found = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(found);
        assertEquals(ManifestationConst.VALID_NAME, found.getManifestationName());
        assertEquals(ManifestationConst.VALID_DESC, found.getDescription());
        assertEquals(ManifestationConst.START.toString(), found.getStartDate());
        assertEquals(ManifestationConst.END.toString(), found.getEndDate());
        assertEquals(ManifestationCategory.SPORTS, found.getCategory());
        assertEquals(3, found.getDays().size());
        assertEquals(12, found.getSectorPrices().size());
        assertEquals(LocationConst.VALID_LOCATION_NAME, found.getLocation().getLocationName());
    }

    @Test
    public void testGetManifestationInfoNotValidId_thenReturnNotFound() {

        ResponseEntity<ManifestationInfoDto> response = restTemplate.exchange("/api/manifestation/manifestationInfo/"+ ManifestationConst.BAD_MAN_ID,
                HttpMethod.GET, createRequestEntity(), ManifestationInfoDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    
}
