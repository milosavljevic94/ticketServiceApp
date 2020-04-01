package com.ftn.services.ticket;


import com.ftn.constants.*;
import com.ftn.dtos.BuyTicketDto;
import com.ftn.dtos.TicketDto;
import com.ftn.dtos.TicketReportDto;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.exceptions.SeatIsNotFreeException;
import com.ftn.model.Reservation;
import com.ftn.model.Ticket;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ManifestationDaysRepository;
import com.ftn.repository.TicketRepository;
import com.ftn.service.TicketService;
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
public class TicketServiceIntegrationTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ManifestationDaysRepository daysRepository;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    @Before
    public void setUp(){
        UserDetails userDetails = userDetailsService.loadUserByUsername(UserConst.DB_USERNAME);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

   @Test
    public void findAllTicketTest_thenReturnTicketList(){
        List<Ticket> result = ticketService.finfAllTickets();

        assertFalse(result.isEmpty());
        assertEquals(TicketConst.DB_TICKET_SIZE, result.size());
    }

    @Test
    public void findOneTicketExistTest_thenReturnTicket(){

        Ticket result = ticketService.findOneTicket(TicketConst.OK_TICKET_ID);

        assertNotNull(result);
        assertEquals(TicketConst.OK_TICKET_ID, result.getId());
        assertEquals(TicketConst.DB_PURCHASE_TIME, result.getPurchaseTime());
        assertEquals(TicketConst.DB_ROW, result.getRowNum());
        assertEquals(TicketConst.DB_COLUMN, result.getSeatNum());
        assertEquals(TicketConst.TICKET_USER, result.getUser().getUsername());
        assertEquals(TicketConst.DAY_NAME, result.getManifestationDays().getName());

    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneLocationNotExistTest_thenThrowException(){

        Ticket result = ticketService.findOneTicket(TicketConst.WRONG_TICKET_ID);
    }

    @Test
    public void addTicketSuccessTest(){

       int sizeBeforeAdd = ticketRepository.findAll().size();
       Ticket result  = ticketService.addTicket(TicketConst.ticketForAddTest());
       int sizeAfterAdd = ticketRepository.findAll().size();

       assertNotNull(result);
       assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);

    }

    @Test(expected = EntityNotFoundException.class)
    public void updateWrongTicketId_thenThrowException(){
        Ticket result = ticketService.updateTicket(TicketConst.wrongDtoForUpdateTest());
    }

    @Test
    public void updateTicketSuccessTest(){

        TicketDto dto = TicketConst.dtoForUpdateTest();

        Ticket result = ticketService.updateTicket(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getRowNum(), result.getRowNum());
        assertEquals(dto.getSeatNum(), result.getSeatNum());
    }

    @Test
    public void deleteTicketSuccessTest(){
        int sizeBeforeDel = ticketRepository.findAll().size();
        ticketService.deleteTicket(TicketConst.OK_TICKET_ID);
        int sizeAfterDel = ticketRepository.findAll().size();

        assertFalse(ticketRepository.findById(TicketConst.OK_TICKET_ID).isPresent());
        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }

    /*@Test
    public void deleteAllTicketSuccessTest(){

        ticketService.deleteAll();

        assertTrue(ticketRepository.findAll().isEmpty());
    }*/


    @Test
    public void whenSeatFree_thenReturnTrue(){

        Boolean result = ticketService.isSeatFree(6,6,1L,1L);

        assertTrue(result);
    }

    @Test
    public void whenSeatIsNotFree_thenReturnFalse(){

        Boolean result = ticketService.isSeatFree(1,1,1L,1L);

        assertFalse(result);
        }


    //Tests for buying tickets.

    @Test(expected = AplicationException.class)
    public void buyTicketNotLoggedIn_thenThrowException(){

        SecurityContextHolder.getContext().setAuthentication(null);

        BuyTicketDto testTicketToBuy = TicketConst.validTicketForBuyTest();
        Ticket result = ticketService.buyTicket(testTicketToBuy);
    }

    @Test(expected = EntityNotFoundException.class)
    public void buyTicketWrongManifestationDay_thenThrowException(){

        BuyTicketDto testTicketToBuy = TicketConst.validTicketForBuyTest();
        testTicketToBuy.setDayId(ManDaysConst.NOT_VALID_ID);

        Ticket result = ticketService.buyTicket(testTicketToBuy);
    }


    @Test(expected = AplicationException.class)
    public void buyTicketWrongRowAndColumn_thenThrowException(){

        BuyTicketDto wrong_row_column = TicketConst.ticketWrongRowAndColumn();
        Ticket result = ticketService.buyTicket(wrong_row_column);
    }

    @Test(expected = SeatIsNotFreeException.class)
    public void buyTicketTakenSeat_thenThrowException(){


        BuyTicketDto takenSeat = TicketConst.ticketForBuyTakenSeat();

        Ticket result = ticketService.buyTicket(takenSeat);
    }

    @Test
    public void buyTicketSuccessTest(){

        Ticket result = ticketService.buyTicket(TicketConst.validTicketForBuyTest());

        assertNotNull(result);
        assertEquals(UserConst.DB_USERNAME, result.getUser().getUsername());
        assertNull(result.getReservation());
        assertTrue(result.getPurchaseConfirmed());
        assertEquals(2, result.getRowNum(), 0);
        assertEquals(5, result.getSeatNum(), 0);
    }

    //Tests for reservation ticket.

    @Test(expected = AplicationException.class)
    public void reserveTicketNotLoggedIn_thenThrowException(){
        SecurityContextHolder.getContext().setAuthentication(null);

        BuyTicketDto testTicketToReserve = TicketConst.validTicketForBuyTest();
        Reservation result = ticketService.reserveTicket(testTicketToReserve);
    }

    @Test(expected = AplicationException.class)
    public void reserveTicketDayLimit_thenThrowException(){

        BuyTicketDto testTicketToReserve = TicketConst.validTicketForBuyTest();

        Reservation result = ticketService.reserveTicket(testTicketToReserve);
    }

    @Test(expected = AplicationException.class)
    public void reserveTicketWrongRowAndColumn_thenThrowException(){

        BuyTicketDto wrong_row_column = TicketConst.ticketWrongRowAndColumn();
        Reservation result = ticketService.reserveTicket(wrong_row_column);
    }


    @Test(expected = SeatIsNotFreeException.class)
    public void reserveTicketTakenSeat_thenThrowException(){

        BuyTicketDto takenSeat = TicketConst.ticketForReservce_takenSeat();
        Reservation result = ticketService.reserveTicket(takenSeat);
    }

    @Test
    public void reserveTicketSuccessTest(){

        BuyTicketDto testTicketToReserve = TicketConst.integrationTest_validToReserve();

        Reservation result = ticketService.reserveTicket(testTicketToReserve);

        assertNotNull(result);
        assertTrue(result.getActive());
        assertEquals(Restrictions.RESERVATION_DURATION, result.getExpDays());
        assertEquals(UserConst.DB_USERNAME, result.getUser().getUsername());
    }

    //Buy reserved ticket tests.

    @Test(expected = AplicationException.class)
    public void buyReservedTicketNotLoggedIn_thenThrowException(){
        SecurityContextHolder.getContext().setAuthentication(null);
        Ticket result = ticketService.buyReservedTicket(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void buyreservedTicket_reservationNotExist_thenThrowException(){

        Ticket result = ticketService.buyReservedTicket(ReservationConst.NOT_VALID_ID);
    }

    @Test(expected = AplicationException.class)
    public void buyreservedTicket_notHaveReservation_thenThrowException(){

        Ticket result = ticketService.buyReservedTicket(ReservationConst.OTHER_USER_RESERVATION);
    }

    @Test
    public void buyReservedTicketSuccessTest(){

        Ticket result = ticketService.buyReservedTicket(ReservationConst.VALID_ID);

        assertNotNull(result);
        assertTrue(result.getPurchaseConfirmed());
        assertEquals(UserConst.DB_USERNAME, result.getUser().getUsername());
    }

    //Ticket of user tests.

    @Test(expected = AplicationException.class)
    public void ticketOfUserNotLoggedIn_thenThrowException(){
        SecurityContextHolder.getContext().setAuthentication(null);
        List<BuyTicketDto> result = ticketService.ticketsOfUser();
    }

    @Test
    public void ticketOfUserSuccessTest(){
        //user with 2 tickets(reserved or purchased).
        List<BuyTicketDto> result = ticketService.ticketsOfUser();

        assertFalse(result.isEmpty());
        assertEquals(4, result.size());
    }

    //Reports tests.

    @Test(expected = AplicationException.class)
    public void makeReportDayLocationBadDate_thenThrowException(){

        TicketReportDto result = ticketService.makeReportDayLocation(TicketConst.OK_TICKET_ID, "2020");
    }

    @Test
    public void makeReportDayLocationSuccessTest(){
        TicketReportDto result = ticketService.makeReportDayLocation(LocationConst.VALID_LOC_ID, "2020-02-10");

        assertNotNull(result);
        assertEquals(2, result.getSoldTicketNumber());
        assertEquals(500.00, result.getProfit(), 0);
    }


    @Test(expected = AplicationException.class)
    public void makeReportMonthLocationBadDate_thenThrowException(){

        TicketReportDto result = ticketService.makeReportMonthLocation(LocationConst.VALID_LOC_ID, "2020");
    }

    @Test
    public void makeReportMonthLocationSuccessTest(){

        TicketReportDto result = ticketService.makeReportMonthLocation(TicketConst.OK_TICKET_ID, "2020-02");

        assertNotNull(result);
        assertEquals(5, result.getSoldTicketNumber());
        assertEquals(1450.00, result.getProfit(), 0);
    }


    @Test(expected = AplicationException.class)
    public void makeReportYearLocationBadDate_thenThrowException(){

        TicketReportDto result = ticketService.makeReportYearLocation(TicketConst.OK_TICKET_ID, "2020-03");
    }

    @Test
    public void makeReportYearLocationSuccessTest(){
        TicketReportDto result = ticketService.makeReportYearLocation(TicketConst.OK_TICKET_ID, "2020");

        assertNotNull(result);
        assertEquals(5, result.getSoldTicketNumber());
        assertEquals(1450.00, result.getProfit(), 0);
    }

    @Test()
    public void makeReportDayManifestationSuccessTest(){
        TicketReportDto resultFirstDay = ticketService.makeReportDayManifestation(1L);

        assertNotNull(resultFirstDay);
        assertEquals(750.00, resultFirstDay.getProfit(), 0);
        assertEquals(3, resultFirstDay.getSoldTicketNumber());

    }

    @Test()
    public void makeReportWholeManifestationSuccessTest(){

        TicketReportDto result = ticketService.makeReportWholeManifestation(1L);

        assertNotNull(result);
        assertEquals(1450.00, result.getProfit(), 0);
        assertEquals(5, result.getSoldTicketNumber());
    }
}
