package com.ftn.controllers;

import com.ftn.constants.LocationConst;
import com.ftn.constants.ManDaysConst;
import com.ftn.constants.ManifestationConst;
import com.ftn.constants.SectorConst;
import com.ftn.dtos.*;
import com.ftn.enums.ManifestationCategory;
import com.ftn.model.Manifestation;
import com.ftn.model.ManifestationDays;
import com.ftn.model.ManifestationSector;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ManifestationDaysRepository;
import com.ftn.repository.ManifestationRepository;
import com.ftn.repository.ManifestationSectorRepository;
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

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ManifestationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ManifestationRepository repository;

    @Autowired
    ManifestationSectorRepository manSectorRepository;

    @Autowired
    ManifestationDaysRepository daysRepository;

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
    public void testGetAllManifestations_thenReturnManifestationsLisst() {

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

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/" + ManifestationConst.OK_MAN_ID,
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testGetManifestationDay_thenReturnManDay() {

        ResponseEntity<ManifestationDayDto> response = restTemplate.exchange("/api/manifestation/manifestationDay/" + ManDaysConst.VALID_ID,
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

        ResponseEntity<ManifestationDayDto> response = restTemplate.exchange("/api/manifestation/manifestationDay/" + ManDaysConst.NOT_VALID_ID,
                HttpMethod.GET, createRequestEntity(), ManifestationDayDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetManifestationInfo_thenReturnInfoDto() {

        ResponseEntity<ManifestationInfoDto> response = restTemplate.exchange("/api/manifestation/manifestationInfo/" + ManifestationConst.OK_MAN_ID,
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

        ResponseEntity<ManifestationInfoDto> response = restTemplate.exchange("/api/manifestation/manifestationInfo/" + ManifestationConst.BAD_MAN_ID,
                HttpMethod.GET, createRequestEntity(), ManifestationInfoDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(statements = "ALTER TABLE manifestation AUTO_INCREMENT = 2")
    public void testAddManifestation_thenReturnAdded() {

        int sizeBeforeAdd = repository.findAll().size();

        ManifestationDto dtoToAdd = ManifestationConst.newDto();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDto> requestEntity = new HttpEntity<>(dtoToAdd, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/addManifestation", HttpMethod.POST,
                requestEntity, ManifestationDto.class);

        ManifestationDto result = response.getBody();

        int sizeAfterAdd = repository.findAll().size();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(result);
        assertEquals(sizeBeforeAdd+1,sizeAfterAdd );

        assertEquals(dtoToAdd.getName(), result.getName());
        assertEquals(dtoToAdd.getDescription(), result.getDescription());
        assertEquals(dtoToAdd.getStartTime(), result.getStartTime());
        assertEquals(dtoToAdd.getEndTime(), result.getEndTime());
        assertEquals(dtoToAdd.getManifestationCategory(), result.getManifestationCategory());
        assertEquals(2, result.getManDaysDto().size());
        assertEquals(dtoToAdd.getLocationId(), result.getLocationId());

        //when test ends, delete added item.
        repository.deleteById(3L);
    }

    @Test
    public void testAddManStartIsAfterEnd_thenReturnConflict() {

        ManifestationDto dtoToAdd = ManifestationConst.newDtoWrongDate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDto> requestEntity = new HttpEntity<>(dtoToAdd, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/addManifestation", HttpMethod.POST,
                requestEntity, ManifestationDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }


    @Test
    public void testAddManWrongLocation_thenReturnNotFound() {

        ManifestationDto dtoToAdd = ManifestationConst.newDtoWrongLocation();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDto> requestEntity = new HttpEntity<>(dtoToAdd, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/addManifestation", HttpMethod.POST,
                requestEntity, ManifestationDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateManifestation_thenReturnUpdated() {
        ManifestationDto dtoToUpdate = ManifestationConst.newDtoForUpdate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDto> requestEntity = new HttpEntity<>(dtoToUpdate, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/updateManifestation", HttpMethod.PUT,
                requestEntity, ManifestationDto.class);

        ManifestationDto result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);

        assertEquals(dtoToUpdate.getName(), result.getName());
        assertEquals(dtoToUpdate.getDescription(), result.getDescription());
        assertEquals(dtoToUpdate.getManifestationCategory(), result.getManifestationCategory());
        assertEquals(dtoToUpdate.getStartTime(), result.getStartTime());
        assertEquals(dtoToUpdate.getEndTime(), result.getEndTime());
    }

    @Test
    public void testUpdateManNotValidId_thenReturnNotFound() {
        ManifestationDto dtoToUpdate = ManifestationConst.newDtoForUpdate();
        dtoToUpdate.setId(ManifestationConst.BAD_MAN_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDto> requestEntity = new HttpEntity<>(dtoToUpdate, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/updateManifestation", HttpMethod.PUT,
                requestEntity, ManifestationDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateManStartIsAfterEnd_thenReturnConflict() {
        ManifestationDto dtoToUpdate = ManifestationConst.newDtoWrongDate();
        dtoToUpdate.setId(ManifestationConst.OK_MAN_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDto> requestEntity = new HttpEntity<>(dtoToUpdate, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/updateManifestation", HttpMethod.PUT,
                requestEntity, ManifestationDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testSetSectorPriceForManifestation_thenReturnManInfo() {
        ManifestationSectorPriceDto priceDto = ManifestationConst.newSectorPrice();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationSectorPriceDto> requestEntity = new HttpEntity<>(priceDto, headers);

        ResponseEntity<ManifestationInfoDto> response = restTemplate.exchange("/api/manifestation/addSectorPrice/" + ManifestationConst.OK_MAN_ID, HttpMethod.PUT,
                requestEntity, ManifestationInfoDto.class);

        ManifestationInfoDto result = response.getBody();

        List<ManifestationSector> prices = manSectorRepository.findAll();
        ManifestationSector lastAdded = prices.get(prices.size() - 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertEquals(priceDto.getDayId(), lastAdded.getManifestationDays().getId());
        assertEquals(priceDto.getSectorId(), lastAdded.getSector().getId());
        assertEquals(priceDto.getPrice(), lastAdded.getPrice());
    }

    @Test
    public void testSetSectorPriceForManWrongId_thenReturnNotFound() {
        ManifestationSectorPriceDto priceDto = ManifestationConst.newSectorPrice();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationSectorPriceDto> requestEntity = new HttpEntity<>(priceDto, headers);

        ResponseEntity<ManifestationInfoDto> response = restTemplate.exchange("/api/manifestation/addSectorPrice/" + ManifestationConst.BAD_MAN_ID, HttpMethod.PUT,
                requestEntity, ManifestationInfoDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void testSetSectorPriceForManWrongDay_thenReturnBadRequest() {
        ManifestationSectorPriceDto priceDto = ManifestationConst.newSectorPrice();
        priceDto.setDayId(ManDaysConst.NOT_VALID_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationSectorPriceDto> requestEntity = new HttpEntity<>(priceDto, headers);

        ResponseEntity<ManifestationInfoDto> response = restTemplate.exchange("/api/manifestation/addSectorPrice/" + ManifestationConst.OK_MAN_ID, HttpMethod.PUT,
                requestEntity, ManifestationInfoDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testSetSectorPriceForManWrongSector_thenReturnBadRequest() {
        ManifestationSectorPriceDto priceDto = ManifestationConst.newSectorPrice();
        priceDto.setSectorId(SectorConst.BAD_ID_SECTOR);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationSectorPriceDto> requestEntity = new HttpEntity<>(priceDto, headers);

        ResponseEntity<ManifestationInfoDto> response = restTemplate.exchange("/api/manifestation/addSectorPrice/" + ManifestationConst.OK_MAN_ID, HttpMethod.PUT,
                requestEntity, ManifestationInfoDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetManifestationPrices_thenReturnPricesList() {

        ResponseEntity<ManifestationSectorPriceDto[]> response = restTemplate.exchange("/api/manifestation/manifestationPrices/" + ManifestationConst.OK_MAN_ID, HttpMethod.GET,
                createRequestEntity(), ManifestationSectorPriceDto[].class);

        List<ManifestationSectorPriceDto> result = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(12, result.size());
    }

    @Test
    public void testGetManifestationPricesWrongManId_thenReturnNotFound() {

        ResponseEntity<ManifestationSectorPriceDto[]> response = restTemplate.exchange("/api/manifestation/manifestationPrices/" + ManifestationConst.BAD_MAN_ID, HttpMethod.GET,
                createRequestEntity(), (Class<ManifestationSectorPriceDto[]>) null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(statements = "ALTER TABLE manifestation_days AUTO_INCREMENT = 5")
    public void testDeleteManifestationDay_thenDeleteDay() {
        //save test item to delete.
        daysRepository.save(new ManifestationDays());

        int daysBeforeDel = daysRepository.findAll().size();

        Long indexOfLast = Long.valueOf(daysBeforeDel);

        ResponseEntity<String> response = restTemplate.exchange("/api/manifestation/deleteDay/" + indexOfLast, HttpMethod.DELETE,
                createRequestEntity(), String.class);

        int daysAfterDel = daysRepository.findAll().size();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(daysBeforeDel - 1, daysAfterDel);
    }

    @Test
    public void testDeleteManifestationDayWrongId_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.exchange("/api/manifestation/deleteDay/" + ManDaysConst.NOT_VALID_ID, HttpMethod.DELETE,
                createRequestEntity(), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(statements = "ALTER TABLE manifestation AUTO_INCREMENT = 2")
    public void testDeleteManifestation_thenDeleteManifestation(){
        //save test item to delete.
        repository.save(new Manifestation());

        int beforeDel = repository.findAll().size();

        ResponseEntity<String> response = restTemplate.exchange("/api/manifestation/" + ManifestationConst.DELETE_ID, HttpMethod.DELETE,
                createRequestEntity(), String.class);

        int afterDel = repository.findAll().size();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(beforeDel - 1, afterDel);
    }

    @Test
    public void testUpdateManifestationDay_thenReturnUpdated() {
        ManifestationDaysDto dayDto = ManDaysConst.newDtoToUpdate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDaysDto> requestEntity = new HttpEntity<>(dayDto, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/updateManDay/" + ManifestationConst.OK_MAN_ID, HttpMethod.PUT,
                requestEntity, ManifestationDto.class);

        ManifestationDto result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertTrue(result.getManDaysDto().stream().anyMatch(day -> day.getName().equals(dayDto.getName())));
    }

    @Test
    public void testUpdateManifestationDayWrongId_thenReturnNotFound(){
        ManifestationDaysDto dayDto = ManDaysConst.newDtoToUpdate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDaysDto> requestEntity = new HttpEntity<>(dayDto, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/updateManDay/" + ManifestationConst.BAD_MAN_ID, HttpMethod.PUT,
                requestEntity, ManifestationDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateNotValidDay_thenReturnBadRequest(){
        ManifestationDaysDto dayDto = ManDaysConst.newDtoToUpdate();
        dayDto.setId(ManDaysConst.NOT_VALID_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<ManifestationDaysDto> requestEntity = new HttpEntity<>(dayDto, headers);

        ResponseEntity<ManifestationDto> response = restTemplate.exchange("/api/manifestation/updateManDay/" + ManifestationConst.OK_MAN_ID, HttpMethod.PUT,
                requestEntity, ManifestationDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
