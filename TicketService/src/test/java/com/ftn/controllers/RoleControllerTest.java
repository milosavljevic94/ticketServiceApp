package com.ftn.controllers;

import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.dtos.RoleDto;
import com.ftn.model.Role;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.RoleRepository;
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
public class RoleControllerTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    TestRestTemplate restTemplate;

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
    public void testGetAllRole_thenReturnRoleList(){

        ResponseEntity<RoleDto[]> response = restTemplate.exchange("/api/role/allRole", HttpMethod.GET,
                createRequestEntity(), RoleDto[].class);
        RoleDto[] rolesDto = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(rolesDto);
        assertNotEquals(0, rolesDto.length);
        assertEquals(2, rolesDto.length);
    }

    @Test
    public void testGetRole_thenReturnRole(){

        ResponseEntity<RoleDto> response = restTemplate.exchange("/api/role/1", HttpMethod.GET,
                createRequestEntity(), RoleDto.class);
        RoleDto rolesDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(rolesDto);
        assertEquals("USER", rolesDto.getRoleName());
    }

    @Test
    public void testGetRoleNotValidId_thenNotFound(){

        ResponseEntity<RoleDto> response = restTemplate.exchange("/api/role/111", HttpMethod.GET,
                createRequestEntity(), RoleDto.class);
        RoleDto rolesDto = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(statements = "ALTER TABLE role AUTO_INCREMENT = 2")
    public void testAddRole_thenReturnAdded(){

        int sizeBeforeAdd = roleRepository.findAll().size();

        RoleDto dtoToAdd = new RoleDto();
        dtoToAdd.setRoleName("SUPER_ADMIN");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<RoleDto> requestEntity = new HttpEntity<>(dtoToAdd, headers);

        ResponseEntity<RoleDto> response = restTemplate.exchange("/api/role/addRole", HttpMethod.POST,
                requestEntity, RoleDto.class);
        RoleDto addedRole = response.getBody();


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(addedRole);

        int sizeAfterAdd = roleRepository.findAll().size();
        assertEquals(sizeBeforeAdd+1, sizeAfterAdd);

        assertEquals(dtoToAdd.getRoleName(), addedRole.getRoleName());

        //delete added role.
        roleRepository.deleteById(3L);
    }


    @Test
    public void testUpdateRole_thenReturnUpdated(){

        RoleDto dtoUpdate = new RoleDto();
        dtoUpdate.setId(2L);
        dtoUpdate.setRoleName("UPDATED_ADMIN");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<RoleDto> request = new HttpEntity<>(dtoUpdate, headers);

        ResponseEntity<RoleDto> response = restTemplate.exchange("/api/role/updateRole", HttpMethod.PUT,
                request, RoleDto.class);
        RoleDto updatedRole = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(updatedRole);
        assertEquals(dtoUpdate.getRoleName(), updatedRole.getRoleName());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testDeleteRole(){

        roleRepository.save(new Role("ROLA_ZA_BRISANJETEST"));

        int sizeBeforeDel = roleRepository.findAll().size();

        ResponseEntity<?> response = restTemplate.exchange("/api/role/3", HttpMethod.DELETE,
                createRequestEntity(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        int sizeAfterDel = roleRepository.findAll().size();
        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }

}
