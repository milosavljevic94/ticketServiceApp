package com.ftn.controllers;

import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.TicketRepository;
import org.junit.Before;
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
public class TicketControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TicketRepository ticketRepository;

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
}
