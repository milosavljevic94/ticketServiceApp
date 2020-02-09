package com.ftn.controllers;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.controller.UserController;
import com.ftn.dtos.RoleDto;
import com.ftn.dtos.UserDto;
import com.ftn.model.Role;
import com.ftn.model.User;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.RoleRepository;
import com.ftn.repository.UserRepository;
import com.ftn.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc restUserMockMvc;

    @Before
    public void init() {
        Role role = new Role("USER");
        roleRepository.save(role);

        User existingUser = new User("existing.email@gmail.com", "Existing", "User", "password", role, true, Collections.emptySet(), Collections.emptySet());
        existingUser.setUsername("existing_username");
        userRepository.save(existingUser);
    }

    @After
    public void destroy() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserController userController = new UserController();
        ReflectionTestUtils.setField(userController, "userService", userService);

        this.restUserMockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsBytes(object);
    }

    @Test
    @Transactional
    public void whenRegistering_thenRegisterUser() throws Exception {
        final int beforeDbSize = userRepository.findAll().size();

        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setRoleName("USER");

        UserDto userDto = new UserDto();
        userDto.setUserName("existing");
        userDto.setFirstName("Pera");
        userDto.setLastName("Peric");
        userDto.setPassword("password");
        userDto.setEmail("novi.email@gmail.com");
        userDto.setUserName("perica");
        userDto.setRole(roleDto);

        restUserMockMvc.perform(post("/api/user/register")
                .contentType("application/json")
                .content(convertObjectToJsonBytes(userDto)))
                .andDo(print())
                .andExpect(status().isOk());

        final List<User> users = userRepository.findAll();
        assertEquals(users.size(), beforeDbSize + 1);

        final User testUser = users.get(users.size() - 1);
        assertEquals(testUser.getEmail(), "novi.email@gmail.com");
        assertEquals(testUser.getFirstName(), "Pera");
        assertEquals(testUser.getLastName(), "Peric");
        assertEquals(testUser.getUsername(), "perica");
        assertEquals(testUser.getRole().getRoleName(), "USER");
        assertEquals(testUser.getActive(), true);

    }
}
