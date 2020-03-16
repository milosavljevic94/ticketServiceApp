package com.ftn.services.reservation;

import com.ftn.constants.ReservationConst;
import com.ftn.constants.UserConst;
import com.ftn.dtos.ReservationDto;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Reservation;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ReservationRepository;
import com.ftn.service.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class ReservationServiceIntegrationTest {


    @Autowired
    private ReservationRepository reservationRepository;


    @Autowired
    private ReservationService reservationService;


    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Before
    public void setUp(){
        UserDetails userDetails = userDetailsService.loadUserByUsername(UserConst.DB_USERNAME);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
               userDetails, userDetails.getPassword(), userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    public void findAllReservationTest_thenReturnReservationList(){
        List<Reservation> list = reservationService.finfAllReservation();

        assertFalse(list.isEmpty());
        assertEquals(ReservationConst.RES_SIZE, list.size());
    }

    @Test
    public void findOneReservationExistTest_thenReturnReservation(){
        Reservation result = reservationService.findOneReservation(ReservationConst.VALID_ID);

        assertNotNull(result);
        assertEquals(ReservationConst.VALID_EXPIRATION, result.getExpDays());
        assertTrue(result.getActive());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneReservationNotExistTest_thenThrowException(){
        Reservation result = reservationService.findOneReservation(ReservationConst.NOT_VALID_ID);
    }

    @Test
    public void addReservationSuccessTest(){
        int sizeBeforeAdd = reservationRepository.findAll().size();
        reservationService.addReservation(ReservationConst.reservationToAdd());
        int sizeAfterAdd = reservationRepository.findAll().size();

        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
    }

    @Test(expected = AplicationException.class)
    public void deleteReservationNotLoggedIn_thenThrovException(){
        SecurityContextHolder.getContext().setAuthentication(null);
        reservationService.deleteReservation(ReservationConst.VALID_ID);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteReservationWrongId_thenThrowException(){

        reservationService.deleteReservation(ReservationConst.NOT_VALID_ID);
    }

    @Test(expected = AplicationException.class)
    public void deleteReservation_UserDontHave_thenThrowException(){

        reservationService.deleteReservation(ReservationConst.OTHER_USER_RESERVATION);
    }

    @Test
    public void deleteReservationSuccessTest(){
        int sizeBeforeDel = reservationRepository.findAll().size();
        reservationService.deleteReservation(ReservationConst.VALID_ID);
        int sizeAfterDel = reservationRepository.findAll().size();

        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }

    @Test
    public void deleteAllReservationsSuccessTest(){

        reservationService.deleteAll();

        assertTrue(reservationRepository.findAll().isEmpty());
    }


    @Test(expected = AplicationException.class)
    public void reservationOfUserNotLoggedIn_thenThrowException(){
        SecurityContextHolder.getContext().setAuthentication(null);
        List<ReservationDto> result = reservationService.reservationOfUser();
    }

    @Test
    public void reservationOfUserSuccessTest(){

        List<ReservationDto> result = reservationService.reservationOfUser();

        assertFalse(result.isEmpty());
        assertEquals(ReservationConst.NUM_RESERVATION_VALID_USER, result.size());
        //all reservations are from test1 user.
        assertTrue(result.stream().allMatch(users -> users.getUser().getUserName().equals(UserConst.DB_USERNAME)));
    }

}
