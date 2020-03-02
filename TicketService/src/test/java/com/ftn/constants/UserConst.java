package com.ftn.constants;

import com.ftn.model.Reservation;
import com.ftn.model.Ticket;
import com.ftn.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
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

    public static SecurityContext returnMockedSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        return securityContext;
    }

    public UserConst() {
    }
}
