package com.ftn.controllers;

import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ReservationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ReservationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ReservationRepository reservationRepository;

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
    public void testGetAllReservation_thenReturnReservationList(){
        // TODO: 3.5.2020 : get all success test
    }

    @Test
    public void testGetReservation_thenReturnReservation(){
        // TODO: 3.5.2020 : get one reservation success test
    }

    @Test
    public void testGetReservationWrongId_thenReturnNotFound(){
        // TODO: 3.5.2020 : get one res, wrong id
    }

    @Test
    public void testCancelReservation_thenDeleteReservation(){
        // TODO: 3.5.2020 : delete res, success test
    }

    /*
    * Metode koje se ne koriste su add, update i delete all.
    * Videti sta sa njima raditi da, li zakomentarisati, obrisati ili ostaviti.
    * Proceniti da li ih treba testirati ili ne.
    * */
}
