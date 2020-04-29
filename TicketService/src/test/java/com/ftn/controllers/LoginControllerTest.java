package com.ftn.controllers;

import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.project.TicketServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class LoginControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void loginTestSuccess_thenReturnLoggedIn(){

        LoginDTO loginDTO = new LoginDTO("testAdmin", "admin");

        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO);

        ResponseEntity<LoggedInUserDTO> response = restTemplate.exchange("/login", HttpMethod.POST,
                requestEntity, LoggedInUserDTO.class);
        LoggedInUserDTO logged = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(logged);
        assertNotNull(logged.getToken());
        assertEquals("testAdmin", logged.getUsername());
        assertEquals("test_admin@gmail.com", logged.getEmail());

    }

    @Test
    public void loginTestWrongCredentials_thenReturnError(){

        LoginDTO loginDTO = new LoginDTO("not_valid", "not_valid");

        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO);

        ResponseEntity<?> response = restTemplate.exchange("/login", HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
