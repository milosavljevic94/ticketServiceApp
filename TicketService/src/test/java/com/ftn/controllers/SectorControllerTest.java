package com.ftn.controllers;

import com.ftn.constants.LocationConst;
import com.ftn.constants.SectorConst;
import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.dtos.SectorDto;
import com.ftn.model.Sector;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.LocationRepository;
import com.ftn.repository.SectorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class SectorControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    LocationRepository locationRepository;

    private String accessToken;

    @Before
    public void login() {
        ResponseEntity<LoggedInUserDTO> login = restTemplate.postForEntity("/login", new LoginDTO("testAdmin", "admin"),
                LoggedInUserDTO.class);

        accessToken = login.getBody().getToken();
    }

    private HttpEntity<Object> createRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        return new HttpEntity<>(headers);
    }


    @Test
    public void testGetAllSectorsForLocation_thenReturnSectorList() {
        ResponseEntity<SectorDto[]> response = restTemplate.exchange("/api/sector/allSectorsForLocation/" + LocationConst.VALID_LOC_ID, HttpMethod.GET,
                createRequestEntity(), SectorDto[].class);
        SectorDto[] result = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertNotEquals(0, result.length);
        assertEquals(4, result.length);
    }

    @Test
    public void testGetAllSectorsLocationNotExist_thenNotFound() {
        ResponseEntity<SectorDto[]> response = restTemplate.exchange("/api/sector/allSectorsForLocation/" + LocationConst.NOT_VALID_LOC_ID, HttpMethod.GET,
                createRequestEntity(), (Class<SectorDto[]>) null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetSector_thenReturnSectorDto() {
        ResponseEntity<SectorDto> response = restTemplate.exchange("/api/sector/" + SectorConst.OK_ID_SECTOR, HttpMethod.GET,
                createRequestEntity(), SectorDto.class);
        SectorDto result = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertEquals(SectorConst.VALID_ROWS, result.getRows());
        assertEquals(SectorConst.VALID_COLUMNS, result.getColumns());
        assertEquals(SectorConst.VALID_NAME, result.getSectorName());
        assertEquals(SectorConst.SEATS_NUM, result.getSeatsNumber());
    }

    @Test
    public void testGetSectorNotValidId_thenNotFound() {
        ResponseEntity<SectorDto> response = restTemplate.exchange("/api/sector/" + SectorConst.BAD_ID_SECTOR, HttpMethod.GET,
                createRequestEntity(), SectorDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(statements = "ALTER TABLE sector AUTO_INCREMENT = 7")
    public void testAddSector_thenReturnCreated() {

        int sizeBeforeAdd = sectorRepository.findAll().size();

        SectorDto forAdd = SectorConst.newDtoToAdd();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<SectorDto> requestEntity = new HttpEntity<>(forAdd, headers);

        ResponseEntity<SectorDto> response = restTemplate.exchange("/api/sector/addSector", HttpMethod.POST,
                requestEntity, SectorDto.class);
        SectorDto result = response.getBody();

        int sizeAfterAdd = sectorRepository.findAll().size();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(result);
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);

        assertEquals(forAdd.getSectorName(), result.getSectorName());
        assertEquals(forAdd.getRows(), result.getRows());
        assertEquals(forAdd.getColumns(), result.getColumns());
        assertEquals(forAdd.getSeatsNumber(), result.getSeatsNumber());

        //delete sector after add test.
        sectorRepository.deleteById(Long.valueOf(sizeAfterAdd));
    }

    @Test
    public void testAddSectorWrongLocation_thenReturnNotFound() {
        SectorDto forAdd = SectorConst.newDtoToAdd();
        forAdd.setLocationId(LocationConst.NOT_VALID_LOC_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<SectorDto> requestEntity = new HttpEntity<>(forAdd, headers);

        ResponseEntity<SectorDto> response = restTemplate.exchange("/api/sector/addSector", HttpMethod.POST,
                requestEntity, SectorDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateSector_thenReturnUpdated() {
        SectorDto forUpdate = SectorConst.newDtoToAdd();
        forUpdate.setId(SectorConst.OK_ID_SECTOR);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<SectorDto> requestEntity = new HttpEntity<>(forUpdate, headers);

        ResponseEntity<SectorDto> response = restTemplate.exchange("/api/sector/updateSector", HttpMethod.PUT,
                requestEntity, SectorDto.class);

        SectorDto result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);

        assertEquals(forUpdate.getSectorName(), result.getSectorName());
        assertEquals(forUpdate.getSeatsNumber(), result.getSeatsNumber());
        assertEquals(forUpdate.getRows(), result.getRows());
        assertEquals(forUpdate.getColumns(), result.getColumns());
    }

    @Test
    public void testUpdateSectorWrongLocation_thenReturnNotFound() {
        SectorDto forUpdate = SectorConst.newDtoToAdd();
        forUpdate.setId(SectorConst.OK_ID_SECTOR);
        forUpdate.setLocationId(LocationConst.NOT_VALID_LOC_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<SectorDto> requestEntity = new HttpEntity<>(forUpdate, headers);

        ResponseEntity<SectorDto> response = restTemplate.exchange("/api/sector/updateSector", HttpMethod.PUT,
                requestEntity, SectorDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateSectorWrongSectorId_thenReturnNotFound() {
        SectorDto forUpdate = SectorConst.newDtoToAdd();
        forUpdate.setId(SectorConst.BAD_ID_SECTOR);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<SectorDto> requestEntity = new HttpEntity<>(forUpdate, headers);

        ResponseEntity<SectorDto> response = restTemplate.exchange("/api/sector/updateSector", HttpMethod.PUT,
                requestEntity, SectorDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(statements = "ALTER TABLE sector AUTO_INCREMENT = 8")
    public void testDeleteSector_thenReturnOk() {

        sectorRepository.save(new Sector());

        int sizeBeforeDel = sectorRepository.findAll().size();

        ResponseEntity<?> response = restTemplate.exchange("/api/sector/" + sizeBeforeDel, HttpMethod.DELETE,
                createRequestEntity(), (Class<String>) null);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        int sizeAfterDel = sectorRepository.findAll().size();
        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }
}
