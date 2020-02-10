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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @MockBean
    UserRepository userRepositoryMocked;

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

    //Test method for find all users.
    @Test
    public void getAllUsersTest(){
        List<User> users = new ArrayList<>();
        User u1 = new User();
        u1.setId(11L);
        u1.setUsername("nsername1");

        User u2 = new User();
        u2.setId(22L);
        u2.setUsername("username2");

        User u3 = new User();
        u3.setId(33L);
        u3.setUsername("username3");

        users.add(u1);
        users.add(u2);
        users.add(u3);


        Mockito.when(userRepositoryMocked.findAll()).thenReturn(users);
        List<User> result = userService.finfAllUsers();

        assertEquals(users.size(), result.size());
        assertEquals(u1.getId(),result.get(0).getId());
        assertEquals(u1.getUsername(),result.get(0).getUsername());


        assertEquals(u2.getId(),result.get(1).getId());
        assertEquals(u2.getUsername(),result.get(1).getUsername());

        assertEquals(u3.getId(),result.get(2).getId());
        assertEquals(u3.getUsername(),result.get(2).getUsername());

    }

    /*
    //Update user test, when email already used
    @Test(expected = AplicationException.class)
    public void updateUser_whenEmailExist(){

        User user2 = new User();
        user2.setId(33L);
        user2.setFirstName("Pera");
        user2.setLastName("Peric");
        user2.setEmail("pera@gmail.com");
        userRepositoryMocked.save(user2);

        UserDto dto = new UserDto();
        dto.setId(33L);
        dto.setEmail("pera@gmail.com");
        dto.setFirstName("Petar");
        dto.setLastName("Petrovic");

        Mockito.when(userRepositoryMocked.findByEmail("pera@gmail.com")).thenReturn(user2);

        userService.updateUserBasicFields(dto);
    }
    */




}
