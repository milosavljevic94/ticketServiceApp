package com.ftn.controllers;

import com.ftn.constants.ReservationConst;
import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.dtos.ReservationDto;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ReservationRepository;
import com.ftn.repository.TicketRepository;
import com.ftn.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ReservationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

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

    private HttpEntity<Object> createRequestEntityNotLoggedIn() {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(headers);
    }

    @Test
    public void testGetAllReservation_thenReturnReservationList(){
        ResponseEntity<ReservationDto[]> response = restTemplate.exchange("/api/reservation/allReservation", HttpMethod.GET,
                createRequestEntity(), ReservationDto[].class);
        ReservationDto[] result = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertNotEquals(0, result.length);
        assertEquals(3, result.length);
    }

    @Test
    public void testGetAllUsersReservations_thenReturnReservationList(){
        ResponseEntity<ReservationDto[]> response = restTemplate.exchange("/api/reservation/allUserReservation", HttpMethod.GET,
                createRequestEntity(), ReservationDto[].class);
        ReservationDto[] result = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertNotEquals(0, result.length);
        assertEquals(1, result.length);
    }

    @Test
    public void testGetAllUsersResNotLoggedIn_thenReturnBadRequest(){
        ResponseEntity<ReservationDto[]> response = restTemplate.exchange("/api/reservation/allUserReservation", HttpMethod.GET,
                createRequestEntityNotLoggedIn(), (Class<ReservationDto[]>) null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetReservation_thenReturnReservation(){
        ResponseEntity<ReservationDto> response = restTemplate.exchange("/api/reservation/" + ReservationConst.VALID_ID, HttpMethod.GET,
                createRequestEntity(), ReservationDto.class);
        ReservationDto result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertEquals(3, result.getTicket().getTicketId(),0);
        assertEquals("test1", result.getUser().getUserName());
        assertEquals(10, result.getExpDays(), 0);
        assertTrue(result.getActive());
    }

    @Test
    public void testGetReservationWrongId_thenReturnNotFound(){
        ResponseEntity<ReservationDto> response = restTemplate.exchange("/api/reservation/" + ReservationConst.NOT_VALID_ID, HttpMethod.GET,
                createRequestEntity(), ReservationDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testCancelReservation_thenDeleteReservation() {

        int sizeBeforeDel = reservationRepository.findAll().size();

        ResponseEntity<?> response = restTemplate.exchange("/api/reservation/cancelReservation/" + "3", HttpMethod.DELETE,
                createRequestEntity(), (Class<String>) null);

        int sizeAfterDel = reservationRepository.findAll().size();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }

    @Test
    public void testCancelReservationNotLoggedIn_thenReturnBadRequest(){
        ResponseEntity<?> response = restTemplate.exchange("/api/reservation/cancelReservation/3", HttpMethod.DELETE,
                createRequestEntityNotLoggedIn(), (Class<String>) null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testCancelReservationDoNotHave_thenReturnBadRequest() {
        ResponseEntity<?> response = restTemplate.exchange("/api/reservation/cancelReservation/1", HttpMethod.DELETE,
                createRequestEntity(), (Class<String>) null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /*
     * Methods that are not used are:  add, update and delete all.
     * If they are added later, then test them.
     */
}
