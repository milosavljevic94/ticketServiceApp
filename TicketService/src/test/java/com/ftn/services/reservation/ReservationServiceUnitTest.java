package com.ftn.services.reservation;


import com.ftn.constants.UserConst;
import com.ftn.dtos.ReservationDto;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.*;
import com.ftn.repository.ReservationRepository;
import com.ftn.service.ReservationService;
import com.ftn.service.TicketService;
import com.ftn.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceUnitTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserService userService;

    @Mock
    private TicketService ticketService;

    @Before
    public void setUp(){

        User user = new User("test@gmail.com", "testUser", "testing", "test123",
                new Role("USER"), true, new HashSet<>(), new HashSet<>());

        Ticket ticket = new Ticket(2, 5, false, LocalDateTime.of(2020,03,01,12,12,00),
                new Reservation(), new ManifestationSector(),
                new ManifestationDays(), new User());

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setExpDays(5);
        reservation.setActive(true);
        reservation.setTicket(ticket);
        reservation.setUser(user);

        user.getReservations().add(reservation);
        user.getTickets().add(ticket);

        ticket.setReservation(reservation);
        ticket.setUser(user);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        User userLogged = UserConst.returnLoggedUserMock();
        SecurityContext securityContext = UserConst.returnMockedSecurityContext();
        SecurityContextHolder.setContext(securityContext);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.findAll()).thenReturn(reservations);

    }

    @Test
    public void findAllReservationTest_thenReturnReservationList(){
        List<Reservation> list = reservationService.finfAllReservation();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    public void findOneReservationExistTest_thenReturnReservation(){
        Long ok_id = 1L;
        Reservation rResult = reservationService.findOneReservation(ok_id);
        assertNotNull(rResult);
        assertEquals(Optional.of(1L).get(), rResult.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneReservationNotExistTest_thenThrowException(){
        Long wrong_id = 22L;
        Reservation result = reservationService.findOneReservation(wrong_id);
    }

    @Test
    public void addReservationSuccessTest(){
        Reservation reservationToAdd = new Reservation();
        reservationService.addReservation(reservationToAdd);

        verify(reservationRepository, times(1)).save(reservationToAdd);
    }

    @Test(expected = AplicationException.class)
    public void deleteReservationNotLoggedIn_thenThrovException(){

        reservationService.deleteReservation(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteReservationWrongId_thenThrowException(){

        Long wrongId = 2345L;

        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());
        reservationService.deleteReservation(wrongId);
    }

    @Test(expected = AplicationException.class)
    public void deleteReservation_UserDontHave_thenThrowException(){

        Long someOtherReservation = 1L;

        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());
        reservationService.deleteReservation(someOtherReservation);
    }

    @Test
    public void deleteReservationSuccessTest(){

        User loggedUserWithReservation = UserConst.returnLoggedUserMock();

        when(userService.getloggedInUser()).thenReturn(loggedUserWithReservation);
        when(reservationRepository.findById(11L)).thenReturn(Optional.ofNullable(loggedUserWithReservation.getReservations().iterator().next()));

        reservationService.deleteReservation(11L);

        verify(reservationRepository, times(1)).deleteById(11L);
        verify(ticketService, times(1)).deleteTicket(11L);
    }


    @Test(expected = AplicationException.class)
    public void reservationOfUserNotLoggedIn_thenThrowException(){

        List<ReservationDto> result = reservationService.reservationOfUser();
    }

    @Test
    public void reservationOfUserSuccessTest(){

        User loggedUserWithReservation = UserConst.returnLoggedUserMock();
        Reservation expected = loggedUserWithReservation.getReservations().iterator().next();

        when(userService.getloggedInUser()).thenReturn(loggedUserWithReservation);
        List<ReservationDto> result = reservationService.reservationOfUser();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expected.getId(), result.iterator().next().getId());
        assertEquals(1, result.size());
    }

}
