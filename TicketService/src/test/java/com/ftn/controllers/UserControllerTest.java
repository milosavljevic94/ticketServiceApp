package com.ftn.controllers;


import com.ftn.constants.UserConst;
import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.dtos.UserDto;
import com.ftn.dtos.UserDtoRes;
import com.ftn.model.User;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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

    @Test
    public void testGetAllUsers_thenReturnUsersList(){
        ResponseEntity<UserDto[]> response = restTemplate.exchange("/api/user/allUsers", HttpMethod.GET,
                createRequestEntity(), UserDto[].class);
        UserDto[] result = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertNotEquals(0, result.length);
        assertEquals(2, result.length);
    }

    @Test
    @Sql(statements = "ALTER TABLE user AUTO_INCREMENT = 2")
    public void testRegister_thenReturnRegisterUser(){
        int sizeBeforeReg = userRepository.findAll().size();

        UserDto userDto = UserConst.newUserToRegister();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<UserDtoRes> response = restTemplate.exchange("/api/user/register", HttpMethod.POST,
                requestEntity, UserDtoRes.class);
        UserDtoRes result = response.getBody();

        int sizeAfterReg = userRepository.findAll().size();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertEquals(sizeBeforeReg + 1, sizeAfterReg);

        assertEquals(userDto.getRole().getRoleName(), result.getRole());
        assertEquals(userDto.getFirstName(), result.getFirstName());
        assertEquals(userDto.getLastName(), result.getLastName());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getUserName(), result.getUserName());
        assertTrue(result.getActive());

        //delete test user after register test.
        userRepository.deleteById(Long.valueOf(sizeAfterReg));
    }

    @Test
    public void testRegisterUsernameOrEmailExist_thenReturnConflict(){
        UserDto userDto = UserConst.registerDtoExistEmail();
        userDto.setUserName(userRepository.findById(1L).get().getUsername());

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<?> response = restTemplate.exchange("/api/user/register", HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRegisterUserInfoNotValid_thenReturnBadRequest(){
        UserDto userDto = UserConst.registerNotValidDto();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<?> response = restTemplate.exchange("/api/user/register", HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRegisterPasswordNotMatching_thenReturnBadRequest(){
        UserDto userDto = UserConst.validDtoUser();
        userDto.setMatchingPassword("notMatchingPasswordTest");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<?> response = restTemplate.exchange("/api/user/register", HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRegisterAdminAlreadyExist_thenReturnBadRequest(){
        UserDto userDto = UserConst.registerDtoAdminRole();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<?> response = restTemplate.exchange("/api/user/register", HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_thenReturnUpdated(){
        UserDto userDto = UserConst.returnDtoForUpdate();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/user/updateUser", HttpMethod.PUT,
                requestEntity, UserDto.class);

        UserDto result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);

        assertEquals(userDto.getFirstName(), result.getFirstName());
        assertEquals(userDto.getLastName(), result.getLastName());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getUserName(), result.getUserName());
    }

    @Test
    public void testUpdateUsernameOrEmailAlreadyExist_thenReturnConflict(){
        UserDto userDto = UserConst.returnDtoForUpdate();
        userDto.setUserName(UserConst.DB_USERNAME);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/user/updateUser", HttpMethod.PUT,
                requestEntity, UserDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testUpdateNotValidId_thenReturnNotFound(){
        UserDto userDto = UserConst.returnDtoForUpdateWrongId();
        userDto.setId(UserConst.NOT_VALID_ID);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/user/updateUser", HttpMethod.PUT,
                requestEntity, UserDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(statements = "ALTER TABLE user AUTO_INCREMENT = 2")
    public void testDeleteUser_thenReturnOk(){

        /*
        First add test item, then delete.
         */
        User testUser = UserConst.returnOneUser();
        testUser.setActive(true);
        userRepository.save(testUser);

        int sizeBeforeDel = userRepository.findAll().size();

        ResponseEntity<?> response = restTemplate.exchange("/api/user/" + sizeBeforeDel, HttpMethod.DELETE,
                createRequestEntity(), (Class<String>) null);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        int sizeAfterDel = userRepository.findAll().size();
        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }
}
