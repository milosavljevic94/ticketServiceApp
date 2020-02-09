package com.ftn.services;


import com.ftn.dtos.RoleDto;
import com.ftn.dtos.UserDto;
import com.ftn.dtos.UserDtoRes;
import com.ftn.exceptions.EmailExistsException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Before
    public void init() {
        Role role = new Role("USER");
        roleRepository.save(role);

        User existingUser = new User("existing.email@gmail.com", "Existing", "User", "password", role, true, Collections.emptySet(), Collections.emptySet());
        existingUser.setUsername("existing_username");
        userRepository.save(existingUser);
    }

    @Test(expected = EmailExistsException.class)
    @Transactional
    public void whenRegistering_thenThrowErrorIfEmailExists() throws EmailExistsException {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setRoleName("USER");

        UserDto userDto = new UserDto();
        userDto.setUserName("existing");
        userDto.setFirstName("Existing");
        userDto.setLastName("User");
        userDto.setPassword("password");
        userDto.setEmail("existing.email@gmail.com");
        userDto.setUserName("existing_username");

        userDto.setRole(roleDto);
        userService.register(userDto); // baca grešku jer postoji user
    }

    @After
    public void destroy() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @Transactional
    public void whenRegistering_thenRegisterUser() throws EmailExistsException {
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
        UserDtoRes res = userService.register(userDto);

        assertEquals(res.getEmail(), "novi.email@gmail.com");
        assertEquals(res.getFirstName(), "Pera");
        assertEquals(res.getLastName(), "Peric");
        assertEquals(res.getUserName(), "perica");
        assertEquals(res.getRole(), "USER");
        assertEquals(res.getActive(), true);

    }
}
