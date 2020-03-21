package com.ftn.constants;

import com.ftn.dtos.RoleDto;
import com.ftn.dtos.UserDto;
import com.ftn.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.mockito.Mockito.mock;

public class UserConst {

    //for integration test

    public static Long DB_ID = 1L;
    public static Long NOT_VALID_ID = 111L;
    public static Long ADMIN_ID = 2L;
    public static String NOT_VALID_USERNAME = "not valid username";

    public static String DB_USERNAME = "test1";
    public static String DB_EMAIL = "test1@gmail.com";
    public static String DB_NAME = "TestName";
    public static String DB_LAST_NAME = "TestLastName";


    public static final String FAKE_USERNAME = "fakeUsername";
    public static final String FAKE_EMAIL = "fake.email@smsms.com";

    public static User userToAdd(){

        User user = new User();
        user.setUsername("testAdd");
        user.setEmail("testAdd@gmail.com");
        user.setActive(true);
        user.setTickets(Collections.emptySet());
        user.setReservations(Collections.emptySet());
        user.setPassword("testaddsifra");
        user.setMatchingPassword("testaddsifra");
        user.setLastName("addlastName");
        user.setFirstName("addFirstname");
        return user;
    }


    public static UserDto registerDtoExistEmail(){

        UserDto userDto = new UserDto();
        userDto.setUserName(DB_USERNAME);
        userDto.setEmail(DB_EMAIL);

        return userDto;
    }


    public static UserDto registerDtoAdminRole(){

        RoleDto roleDto = new RoleDto();
        roleDto.setId(2L);
        roleDto.setRoleName("ADMIN");

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

    public static UserDto validDtoUser(){

        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setRoleName("USER");

        UserDto userDto = new UserDto();
        userDto.setUserName("testUsername");
        userDto.setEmail("testEmail.com");
        userDto.setPassword("testPassword");
        userDto.setMatchingPassword("testPassword");
        userDto.setFirstName("testName");
        userDto.setLastName("testLastName");
        userDto.setRole(roleDto);

        return userDto;
    }

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
        existingUser.setUsername("testUsername");
        existingUser.setEmail("test@gmail.com");
        return existingUser;
    }

    public static User returnLoggedUserMock(){

        ManifestationDays manifestationDays = new ManifestationDays();
        manifestationDays.setId(1L);

        ManifestationSector manifestationSector = new ManifestationSector();
        manifestationSector.setId(1L);

        Ticket ticket = new Ticket();
        ticket.setId(11L);
        ticket.setRowNum(2);
        ticket.setSeatNum(8);
        ticket.setManifestationSector(manifestationSector);
        ticket.setManifestationDays(manifestationDays);

        Ticket ticket2 = new Ticket();
        ticket2.setId(11L);
        ticket2.setRowNum(3);
        ticket2.setSeatNum(9);
        ticket2.setManifestationSector(manifestationSector);
        ticket2.setManifestationDays(manifestationDays);

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
