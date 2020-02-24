package com.ftn.repository;

import com.ftn.constants.UserConst;
import com.ftn.model.Role;
import com.ftn.model.User;
import com.ftn.project.TicketServiceApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

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
    }

    @Test
    @Transactional
    public void whenUsernameExist_thenReturnUser(){
        User u = userRepository.findByUsername(UserConst.REAL_USERNAME);
        assertNotNull(u);
        assertEquals(UserConst.REAL_USERNAME, u.getUsername());
    }

    @Test
    @Transactional
    public void whenUsernameNotExist_thenReturnNull(){
        User u = userRepository.findByUsername(UserConst.FAKE_USERNAME);
        assertNull(u);
    }

    @Test
    @Transactional
    public void whenEmailExist_thenReturnUser(){
        User u = userRepository.findByEmail(UserConst.REAL_EMAIL);
        assertNotNull(u);
        assertEquals(UserConst.REAL_EMAIL, u.getEmail());
    }

    @Test
    @Transactional
    public void whenEmailNotExist_thenReturnNull(){
        User u = userRepository.findByUsername(UserConst.FAKE_EMAIL);
        assertNull(u);
    }

    @Test
    @Transactional
    public void whenEmailOrUsernameExist_thenReturnUser(){
        User u = userRepository.findByUsernameOrEmail(UserConst.REAL_USERNAME, UserConst.REAL_EMAIL);
        assertNotNull(u);
    }

    @Test
    @Transactional
    public void whenEmailOrUsernameNotExist_thenReturnNull(){
        User u = userRepository.findByUsernameOrEmail(UserConst.FAKE_USERNAME, UserConst.FAKE_EMAIL);
        assertNull(u);
    }
}
