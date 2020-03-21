package com.ftn.repository;

import com.ftn.constants.UserConst;
import com.ftn.model.User;
import com.ftn.project.TicketServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void whenUsernameExist_thenReturnUser(){
        User u = userRepository.findByUsername(UserConst.DB_USERNAME);

        assertNotNull(u);
        assertEquals(UserConst.DB_USERNAME, u.getUsername());
    }

    @Test
    public void whenUsernameNotExist_thenReturnNull(){
        User u = userRepository.findByUsername(UserConst.FAKE_USERNAME);

        assertNull(u);
    }

    @Test
    public void whenEmailExist_thenReturnUser(){
        User u = userRepository.findByEmail(UserConst.DB_EMAIL);

        assertNotNull(u);
        assertEquals(UserConst.DB_EMAIL, u.getEmail());
    }

    @Test
    public void whenEmailNotExist_thenReturnNull(){
        User u = userRepository.findByUsername(UserConst.FAKE_EMAIL);

        assertNull(u);
    }

    @Test
    public void whenEmailOrUsernameExist_thenReturnUser(){
        User u = userRepository.findByUsernameOrEmail(UserConst.DB_USERNAME, UserConst.DB_EMAIL);

        assertNotNull(u);
    }

    @Test
    public void whenEmailOrUsernameNotExist_thenReturnNull(){
        User u = userRepository.findByUsernameOrEmail(UserConst.FAKE_USERNAME, UserConst.FAKE_EMAIL);

        assertNull(u);
    }
}
