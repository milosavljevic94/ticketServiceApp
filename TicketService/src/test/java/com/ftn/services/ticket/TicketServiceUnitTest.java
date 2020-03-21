package com.ftn.services.ticket;

import com.ftn.constants.TicketConst;
import com.ftn.constants.UserConst;
import com.ftn.dtos.BuyTicketDto;
import com.ftn.dtos.TicketDto;
import com.ftn.dtos.TicketReportDto;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.exceptions.SeatIsNotFreeException;
import com.ftn.model.*;
import com.ftn.repository.TicketRepository;
import com.ftn.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceUnitTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepositoryMocked;

    @Mock
    private UserService userService;

    @Mock
    private ManifestationDayService manifestationDayService;

    @Mock
    private ManifestationSectorService manSectorService;

    @Mock
    private ManifestationService manifestationService;

    @Mock
    private ReservationService reservationService;


    @Before
    public void setUp() {

        Ticket ticket = new Ticket(5, 7, true, LocalDateTime.now(), new Reservation(), new ManifestationSector(),
                new ManifestationDays(), new User());
        ticket.setId(TicketConst.OK_TICKET_ID);


        ArrayList<Ticket> tickets = new ArrayList<>();

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        tickets.add(ticket);
        tickets.add(ticket2);

        when(ticketRepositoryMocked.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(ticketRepositoryMocked.findAll()).thenReturn(tickets);
    }

    @Test
    public void findAllTicketTest_thenReturnTicketList(){
        List<Ticket> result = ticketService.finfAllTickets();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void findOneTicketExistTest_thenReturnTicket(){

        Ticket result = ticketService.findOneTicket(TicketConst.OK_TICKET_ID);
        assertNotNull(result);
        assertEquals(TicketConst.OK_TICKET_ID, result.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneLocationNotExistTest_thenThrowException(){

        Ticket result = ticketService.findOneTicket(TicketConst.WRONG_TICKET_ID);
    }

    @Test
    public void addTicketSuccessTest(){

        Ticket result  = ticketService.addTicket(TicketConst.ticketForAddTest());

        assertNotNull(result);
        assertEquals(TicketConst.ticketForAddTest().getId(), result.getId());
        verify(ticketRepositoryMocked, times(1)).save(any(Ticket.class));
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

        verify(ticketRepositoryMocked, times(1)).save(any(Ticket.class));
    }

    @Test
    public void deleteTicketSuccessTest(){

        ticketService.deleteTicket(TicketConst.OK_TICKET_ID);

        verify(ticketRepositoryMocked, times(1)).deleteById(TicketConst.OK_TICKET_ID);
    }

    @Test
    public void deleteAllTicketSuccessTest(){

        ticketService.deleteAll();

        verify(ticketRepositoryMocked, times(1)).deleteAll();
    }


    @Test
    public void whenSeatFree_thenReturnTrue(){

        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.ticketsForCheckFreeSeats());

        Boolean result = ticketService.isSeatFree(1,1,1L,1L);

        assertTrue(result);
    }

    @Test
    public void whenSeatIsNotFree_thenReturnFalse(){

        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.ticketsForCheckFreeSeats());

        Boolean result = ticketService.isSeatFree(3,3,1L,1L);

        assertFalse(result);
    }


    //Tests for buying tickets.

    @Test(expected = AplicationException.class)
    public void buyTicketNotLoggedIn_thenThrowException(){

        BuyTicketDto testTicketToBuy = TicketConst.validTicketForBuyTest();
        Ticket result = ticketService.buyTicket(testTicketToBuy);
    }

    @Test(expected = EntityNotFoundException.class)
    public void buyTicketWrongManifestationDay_thenThrowException(){

        Long WRONG_ID_OF_DAY = 22L;

        ManifestationDays md = new ManifestationDays();
        md.setId(1L);

        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());

        BuyTicketDto testTicketToBuy = TicketConst.validTicketForBuyTest();
        testTicketToBuy.setDayId(WRONG_ID_OF_DAY);

        when(manifestationDayService.findOneManifestationDays(testTicketToBuy.getDayId())).thenThrow(EntityNotFoundException.class);

        Ticket result = ticketService.buyTicket(testTicketToBuy);
    }

    @Test(expected = AplicationException.class)
    public void buyTicketWrongRowAndColumn_thenThrowException(){
        ManifestationDays md = new ManifestationDays();
        md.setId(1L);

        Sector sector = new Sector("testSector", 5, 10, new Location());
        sector.setId(1L);


        ManifestationSector ms = new ManifestationSector();
        ms.setId(1L);
        ms.setSector(sector);


        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());

        BuyTicketDto wrong_row_column = TicketConst.ticketForBuyWrongRowColumn();

        when(manifestationDayService.findOneManifestationDays(wrong_row_column.getDayId())).thenReturn(md);
        when(manSectorService.getSectorPriceById(wrong_row_column.getWantedSeat().getManSectorId())).thenReturn(ms);

        Ticket result = ticketService.buyTicket(wrong_row_column);
    }

    @Test(expected = SeatIsNotFreeException.class)
    public void buyTicketTakenSeat_thenThrowException(){
        ManifestationDays md = new ManifestationDays();
        md.setId(1L);

        Sector sector = new Sector("testSector", 5, 10, new Location());
        sector.setId(1L);


        ManifestationSector ms = new ManifestationSector();
        ms.setId(1L);
        ms.setSector(sector);


        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());

        BuyTicketDto takenSeat = TicketConst.ticketForBuyTakenSeat();

        when(manifestationDayService.findOneManifestationDays(takenSeat.getDayId())).thenReturn(md);
        when(manSectorService.getSectorPriceById(takenSeat.getWantedSeat().getManSectorId())).thenReturn(ms);
        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.ticketsForCheckFreeSeats());

        Ticket result = ticketService.buyTicket(takenSeat);
    }

    @Test
    public void buyTicketSuccessTest(){

        ManifestationDays md = new ManifestationDays();
        md.setId(1L);

        Sector sector = new Sector("testSector", 5, 10, new Location());
        sector.setId(1L);


        ManifestationSector ms = new ManifestationSector();
        ms.setId(1L);
        ms.setSector(sector);

        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());

        BuyTicketDto testTicketToBuy = TicketConst.validTicketForBuyTest();

        when(manifestationDayService.findOneManifestationDays(testTicketToBuy.getDayId())).thenReturn(md);

        when(manSectorService.getSectorPriceById(testTicketToBuy.getWantedSeat().getManSectorId())).thenReturn(ms);

        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.ticketsForCheckFreeSeats());

        Ticket result = ticketService.buyTicket(testTicketToBuy);

        assertNotNull(result);
        assertEquals(UserConst.MOCK_USER_USERNAME, result.getUser().getUsername());
        assertNull(result.getReservation());
        assertTrue(result.getPurchaseConfirmed());
        verify(ticketRepositoryMocked, times(1)).save(result);
    }

    //Tests for reservation ticket.

    @Test(expected = AplicationException.class)
    public void reserveTicketNotLoggedIn_thenThrowException(){

        BuyTicketDto testTicketToReserve = TicketConst.validTicketForBuyTest();
        Reservation result = ticketService.reserveTicket(testTicketToReserve);
    }

    @Test(expected = AplicationException.class)
    public void reserveTicketDayLimit_thenThrowException(){

        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());

        BuyTicketDto testTicketToReserve = TicketConst.validTicketForBuyTest();

        when(manifestationDayService.findOneManifestationDays(testTicketToReserve.getDayId())).thenReturn(TicketConst.dayForReservationLimitTest());

        Reservation result = ticketService.reserveTicket(testTicketToReserve);
    }

    @Test(expected = AplicationException.class)
    public void reserveTicketWrongRowAndColumn_thenThrowException(){

        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());

        BuyTicketDto wrong_row_column = TicketConst.ticketForBuyWrongRowColumn();

        when(manifestationDayService.findOneManifestationDays(wrong_row_column.getDayId())).thenReturn(TicketConst.validManDay());
        when(manSectorService.getSectorPriceById(wrong_row_column.getWantedSeat().getManSectorId())).thenReturn(TicketConst.validManSector());

        Reservation result = ticketService.reserveTicket(wrong_row_column);
    }


    @Test(expected = SeatIsNotFreeException.class)
    public void reserveTicketTakenSeat_thenThrowException(){

        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());

        BuyTicketDto takenSeat = TicketConst.ticketForBuyTakenSeat();

        when(manifestationDayService.findOneManifestationDays(takenSeat.getDayId())).thenReturn(TicketConst.validManDay());
        when(manSectorService.getSectorPriceById(takenSeat.getWantedSeat().getManSectorId())).thenReturn(TicketConst.validManSector());
        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.ticketsForCheckFreeSeats());

        Reservation result = ticketService.reserveTicket(takenSeat);
    }

    @Test
    public void reserveTicketSuccessTest(){

        when(userService.getloggedInUser()).thenReturn(UserConst.returnLoggedUserMock());

        BuyTicketDto testTicketToReserve = TicketConst.validTicketForBuyTest();

        when(manifestationDayService.findOneManifestationDays(testTicketToReserve.getDayId())).thenReturn(TicketConst.validManDay());
        when(manSectorService.getSectorPriceById(testTicketToReserve.getWantedSeat().getManSectorId())).thenReturn(TicketConst.validManSector());
        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.ticketsForCheckFreeSeats());

        Reservation result = ticketService.reserveTicket(testTicketToReserve);

        assertNotNull(result);
        assertTrue(result.getActive());

        verify(ticketRepositoryMocked, times(1)).save(result.getTicket());
        verify(reservationService, times(2)).addReservation(any(Reservation.class));
    }

    //Buy reserved ticket tests.

    @Test(expected = AplicationException.class)
    public void buyReservedTicketNotLoggedIn_thenThrowException(){

        Ticket result = ticketService.buyReservedTicket(1L);
    }

    @Test(expected = AplicationException.class)
    public void buyreservedTicket_notHaveReservation_thenThrowException(){
        User currentUser = UserConst.returnLoggedUserMock();

        when(userService.getloggedInUser()).thenReturn(currentUser);
        when(reservationService.findOneReservation(anyLong())).thenReturn(TicketConst.wrongReservation());

        Ticket result = ticketService.buyReservedTicket(1L);
    }

    @Test
    public void buyReservedTicketSuccessTest(){

        User currentUser = UserConst.returnLoggedUserMock();

        when(userService.getloggedInUser()).thenReturn(currentUser);
        when(reservationService.findOneReservation(anyLong())).thenReturn(currentUser.getReservations().iterator().next());

        Ticket result = ticketService.buyReservedTicket(1L);

        assertNotNull(result);
        assertTrue(result.getPurchaseConfirmed());

        verify(ticketRepositoryMocked, times(1)).save(result);
    }

    //Ticket of user tests.

    @Test(expected = AplicationException.class)
    public void ticketOfUserNotLoggedIn_thenThrowException(){

        List<BuyTicketDto> result = ticketService.ticketsOfUser();
    }

    @Test
    public void ticketOfUserSuccessTest(){

        //user with 2 tickets.
        User currentUser = UserConst.returnLoggedUserMock();
        when(userService.getloggedInUser()).thenReturn(currentUser);

        List<BuyTicketDto> result = ticketService.ticketsOfUser();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    //Reports tests.

    @Test(expected = AplicationException.class)
    public void makeReportDayLocationBadDate_thenThrowException(){

        TicketReportDto result = ticketService.makeReportDayLocation(TicketConst.OK_TICKET_ID, "2020");
    }

    @Test
    public void makeReportDayLocationSuccessTest(){

        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.returnValidTickets());
        TicketReportDto result = ticketService.makeReportDayLocation(TicketConst.OK_TICKET_ID, "2020-03-01");

        assertNotNull(result);
        assertEquals(2, result.getSoldTicketNumber());
        assertEquals(350.00, result.getProfit(), 0);
    }


    @Test(expected = AplicationException.class)
    public void makeReportMonthLocationBadDate_thenThrowException(){

        TicketReportDto result = ticketService.makeReportMonthLocation(TicketConst.OK_TICKET_ID, "2020");
    }

    @Test
    public void makeReportMonthLocationSuccessTest(){

        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.returnValidTickets());
        TicketReportDto result = ticketService.makeReportMonthLocation(TicketConst.OK_TICKET_ID, "2020-03");

        assertNotNull(result);
        assertEquals(2, result.getSoldTicketNumber());
        assertEquals(350.00, result.getProfit(), 0);
    }


    @Test(expected = AplicationException.class)
    public void makeReportYearLocationBadDate_thenThrowException(){

        TicketReportDto result = ticketService.makeReportYearLocation(TicketConst.OK_TICKET_ID, "2020-03");
    }

    @Test
    public void makeReportYearLocationSuccessTest(){

        when(ticketRepositoryMocked.findAll()).thenReturn(TicketConst.returnValidTickets());
        TicketReportDto result = ticketService.makeReportYearLocation(TicketConst.OK_TICKET_ID, "2020");

        assertNotNull(result);
        assertEquals(2, result.getSoldTicketNumber());
        assertEquals(350.00, result.getProfit(), 0);
    }

    @Test()
    public void makeReportDayManifestationSuccessTest(){

        Manifestation validManifestation = TicketConst.manifestationWithValidDays();

        when(manifestationService.findOneManifestation(1L)).thenReturn(validManifestation);

        TicketReportDto resultFirstDay = ticketService.makeReportDayManifestation(1L, 1L);
        TicketReportDto resultSecondDay = ticketService.makeReportDayManifestation(1L, 2L);

        assertNotNull(resultFirstDay);
        assertNotNull(resultSecondDay);

        assertEquals(350.00, resultFirstDay.getProfit(), 0);
        assertEquals(2, resultFirstDay.getSoldTicketNumber());

        assertEquals(450.00, resultSecondDay.getProfit(), 0);
        assertEquals(1, resultSecondDay.getSoldTicketNumber());
    }

    @Test()
    public void makeReportWholeManifestationSuccessTest(){

        Manifestation validManifestation = TicketConst.manifestationWithValidDays();

        when(manifestationService.findOneManifestation(1L)).thenReturn(validManifestation);

        TicketReportDto result = ticketService.makeReportWholeManifestation(1L);

        assertNotNull(result);
        assertEquals(800.00, result.getProfit(), 0);
        assertEquals(3, result.getSoldTicketNumber());
    }
}
