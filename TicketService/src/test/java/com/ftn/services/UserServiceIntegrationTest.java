package com.ftn.services;

import com.ftn.constants.UserConst;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.User;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.UserRepository;
import com.ftn.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    //not found by Id
    @Test(expected = EntityNotFoundException.class)
    public void userIdNotExistsTest(){
        userService.findOneUser(323L);
    }


    @Test
    public void userIdExistsTest(){
        long id = UserConst.ID;

        User user = userService.findOneUser(id);
        System.out.println("Username je : "+ user.getUsername());
        assertEquals((Long)id, user.getId());
        assertEquals(UserConst.USERNAME,user.getUsername());
        assertEquals(UserConst.FIRST_NAME,user.getFirstName());
        assertEquals(UserConst.LAST_NAME, user.getLastName());
        assertEquals(UserConst.EMAIL,user.getEmail());

    }
}

