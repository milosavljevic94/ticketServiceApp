package com.ftn.constants;

import com.ftn.dtos.RoleDto;
import com.ftn.dtos.UserDto;
import com.ftn.model.Reservation;
import com.ftn.model.Role;
import com.ftn.model.Ticket;
import com.ftn.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;

public class UserConst {

    public static final String REAL_USERNAME = "existing_username";
    public static final String FAKE_USERNAME = "username233";
    public static final String REAL_EMAIL = "existing.email@gmail.com";
    public static final String FAKE_EMAIL = "fake.email@smsms.com";
    public static final int ID = 2;

    public static final String USERNAME = "stefaBot";
    public static final String FIRST_NAME = "Stefan";
    public static final String LAST_NAME = "Stefic";
    public static final String EMAIL = "stefa@gmail.com";



    //for unit testing

    public static Long MOCK_USER_ID = 1L;
    public static String  MOCK_USER_EMAIL = "testUser@gmail.com";
    public static String  MOCK_USER_USERNAME = "testUsername";
    public static String  MOCK_USER_PASS = "test123";

    public static List<User> allUsers(){
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user1.setId(2L);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        return users;
    }

    public static User returnOneUser(){
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testUsername");
        existingUser.setEmail("test@gmail.com");
        return existingUser;
    }

    public static User returnLoggedUserMock(){

        Ticket ticket = new Ticket();
        ticket.setId(11L);
        Ticket ticket2 = new Ticket();
        ticket.setId(11L);

        Set<Ticket> ticketsOfUser = new HashSet<>();
        ticketsOfUser.add(ticket);
        ticketsOfUser.add(ticket2);

        Reservation reservation = new Reservation();
        reservation.setId(11L);
        reservation.setActive(true);
        reservation.setTicket(ticket);

        Set<Reservation> reservationSet = new HashSet<>();
        reservationSet.add(reservation);

        User user = new User();
        user.setId(UserConst.MOCK_USER_ID);
        user.setEmail(UserConst.MOCK_USER_EMAIL);
        user.setUsername(MOCK_USER_USERNAME);
        user.setPassword(MOCK_USER_PASS);
        user.setActive(true);
        user.setReservations(reservationSet);
        user.setTickets(ticketsOfUser);


        reservation.setUser(user);

        return user;
    }

    public static UserDto returnDtoForUpdate(){
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUserName("testNewUsername");
        userDto.setEmail("testNewEmail.com");
        userDto.setFirstName("testNewName");
        userDto.setLastName("testNewLastName");

        return userDto;
    }

    public static Role returnRoleUser(){
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("USER");

        return role;
    }

    public static UserDto returnDtoForRegister(){

        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setRoleName("USER");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUserName("testUsername");
        userDto.setEmail("testEmail.com");
        userDto.setPassword("testPassword");
        userDto.setMatchingPassword("testPassword");
        userDto.setFirstName("testName");
        userDto.setLastName("testLastName");
        userDto.setRole(roleDto);

        return userDto;
    }

    public static SecurityContext returnMockedSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        return securityContext;
    }

    public UserConst() {
    }
}
