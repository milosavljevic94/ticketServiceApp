package com.ftn.constants;

import com.ftn.dtos.BuyTicketDto;
import com.ftn.dtos.SeatWithPriceDto;
import com.ftn.dtos.TicketDto;
import com.ftn.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketConst {

    //integration testing.
    public static int DB_TICKET_SIZE = 5;
    public static LocalDateTime DB_PURCHASE_TIME = LocalDateTime.of(2020,02,10, 10,00,00);
    public static Integer DB_ROW = 1;
    public static Integer DB_COLUMN = 1;
    public static String DAY_NAME = "Dan1 man1";
    public static String TICKET_USER = "test1";


    public static BuyTicketDto ticketWrongRowAndColumn(){

        SeatWithPriceDto seatWithPriceDto = new SeatWithPriceDto();
        seatWithPriceDto.setManSectorId(1L);
        seatWithPriceDto.setRow(45);
        seatWithPriceDto.setSeatNumber(145);


        BuyTicketDto buyTicketDto = new BuyTicketDto();
        buyTicketDto.setDayId(2L);
        buyTicketDto.setWantedSeat(seatWithPriceDto);

        return buyTicketDto;
    }


    public static BuyTicketDto ticketForReserve_takenSeat(){

        SeatWithPriceDto seatWithPriceDto = new SeatWithPriceDto();
        seatWithPriceDto.setManSectorId(2L);
        seatWithPriceDto.setRow(6);
        seatWithPriceDto.setSeatNumber(6);


        BuyTicketDto buyTicketDto = new BuyTicketDto();
        buyTicketDto.setDayId(2L);
        buyTicketDto.setWantedSeat(seatWithPriceDto);

        return buyTicketDto;
    }

    public static BuyTicketDto integrationTest_validToReserve(){

        SeatWithPriceDto seatWithPriceDto = new SeatWithPriceDto();
        seatWithPriceDto.setManSectorId(2L);
        seatWithPriceDto.setRow(7);
        seatWithPriceDto.setSeatNumber(7);


        BuyTicketDto buyTicketDto = new BuyTicketDto();
        buyTicketDto.setDayId(2L);
        buyTicketDto.setWantedSeat(seatWithPriceDto);

        return buyTicketDto;
    }


    //for unit testing.
    public static Long OK_TICKET_ID = 1L;
    public static Long WRONG_TICKET_ID = 14455L;


    public static Ticket ticketForAddTest(){
        Ticket t = new Ticket();
        t.setId(22L);
        return t;
    }

    public static TicketDto dtoForUpdateTest(){
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(OK_TICKET_ID);
        ticketDto.setSeatNum(2);
        ticketDto.setRowNum(4);

        return ticketDto;
    }

    public static TicketDto wrongDtoForUpdateTest() {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(WRONG_TICKET_ID);
        ticketDto.setSeatNum(2);
        ticketDto.setRowNum(4);

        return ticketDto;
    }

    public static List<Ticket> ticketsForCheckFreeSeats(){

        Sector sector = new Sector();
        sector.setId(1L);

        ManifestationSector ms = new ManifestationSector();
        ms.setId(1L);
        ms.setSector(sector);

        ManifestationDays md = new ManifestationDays();
        md.setId(1L);

        List<Ticket> ticketsFree = new ArrayList<>();
        Ticket ticket = new Ticket(3, 3, false, LocalDateTime.now(), new Reservation(),
                ms, md, new User());
        ticket.setId(3L);

        ticketsFree.add(ticket);

        return ticketsFree;
    }

    public static BuyTicketDto validTicketForBuyTest(){

        SeatWithPriceDto seatWithPriceDto = new SeatWithPriceDto();
        seatWithPriceDto.setManSectorId(1L);
        seatWithPriceDto.setRow(2);
        seatWithPriceDto.setSeatNumber(5);


        BuyTicketDto buyTicketDto = new BuyTicketDto();
        buyTicketDto.setDayId(1L);
        buyTicketDto.setWantedSeat(seatWithPriceDto);

        return buyTicketDto;
    }

    public static BuyTicketDto ticketForBuyWrongRowColumn(){

        SeatWithPriceDto seatWithPriceDto = new SeatWithPriceDto();
        seatWithPriceDto.setManSectorId(1L);
        seatWithPriceDto.setRow(6);
        seatWithPriceDto.setSeatNumber(18);


        BuyTicketDto buyTicketDto = new BuyTicketDto();
        buyTicketDto.setDayId(1L);
        buyTicketDto.setWantedSeat(seatWithPriceDto);

        return buyTicketDto;
    }

    public static BuyTicketDto ticketForBuyTakenSeat(){

        SeatWithPriceDto seatWithPriceDto = new SeatWithPriceDto();
        seatWithPriceDto.setManSectorId(1L);
        seatWithPriceDto.setRow(3);
        seatWithPriceDto.setSeatNumber(3);


        BuyTicketDto buyTicketDto = new BuyTicketDto();
        buyTicketDto.setDayId(1L);
        buyTicketDto.setPurchaseConfirmed(false);
        buyTicketDto.setPurchaseTime(LocalDateTime.now());
        buyTicketDto.setWantedSeat(seatWithPriceDto);

        return buyTicketDto;
    }

    public static ManifestationDays dayForReservationLimitTest(){

        ManifestationDays md = new ManifestationDays("testDay","test description", LocalDateTime.now(), new Manifestation(), new HashSet<>(), new HashSet<>());
        md.setId(1L);

        return md;
    }

    public static ManifestationDays validManDay(){

        ManifestationDays md = new ManifestationDays("validtestDay","valid test description", LocalDateTime.of(2020,05,02,20,20,00), new Manifestation(), new HashSet<>(), new HashSet<>());
        md.setId(1L);

        return md;
    }

    public static ManifestationSector validManSector(){

        Sector sector = new Sector("testSector", 5, 10, new Location());
        sector.setId(1L);


        ManifestationSector ms = new ManifestationSector();
        ms.setId(1L);
        ms.setSector(sector);

        return ms;
    }

    public static Reservation wrongReservation(){
        Reservation reservation = new Reservation();
        reservation.setId(23L);
        return reservation;
    }

    //for report unit tests.

    public static Long OK_LOCATION_ID = 1L;

    public static List<Ticket> returnValidTickets(){

        Location l = new Location();
        l.setId(OK_LOCATION_ID);

        Sector s = new Sector();
        s.setLocation(l);

        ManifestationSector ms = new ManifestationSector();
        ms.setPrice(200.00);
        ms.setSector(s);

        ManifestationSector ms2 = new ManifestationSector();
        ms2.setPrice(150.00);
        ms2.setSector(s);

        Ticket ticket1 = new Ticket(5, 7, true, LocalDateTime.of(2020,03,01,12,12,00),
                new Reservation(), ms, new ManifestationDays(), new User());
        ticket1.setId(11L);

        Ticket ticket2 = new Ticket(3, 5, true, LocalDateTime.of(2020,03,01,12,12,00),
                new Reservation(), ms2, new ManifestationDays(), new User());
        ticket2.setId(22L);
        ArrayList<Ticket> tickets = new ArrayList<>();


        tickets.add(ticket1);
        tickets.add(ticket2);

        return tickets;
    }

    //manifestation for report, with 2 days.

    public static Manifestation manifestationWithValidDays(){

        //first day with two tickets
        ManifestationDays md1 = new ManifestationDays();
        md1.setId(1L);
        Set<Ticket> tickets = new HashSet<>();
        tickets.addAll(returnValidTickets());
        md1.setTickets(tickets);

        //second day with one ticket
        ManifestationSector ms2 = new ManifestationSector();
        ms2.setPrice(450.00);

        Ticket ticket3 = new Ticket(3, 5, true, LocalDateTime.of(2020,03,01,12,12,00),
                new Reservation(), ms2, new ManifestationDays(), new User());
        ticket3.setId(3L);

        Set<Ticket> tickets3 = new HashSet<>();
        tickets3.add(ticket3);

        ManifestationDays md2 = new ManifestationDays();
        md2.setId(2L);
        md2.setTickets(tickets3);

        Set<ManifestationDays> days = new HashSet<>();
        days.add(md1);
        days.add(md2);

        Manifestation m = new Manifestation();
        m.setId(1L);
        m.setManifestationDays(days);

        return m;
    }


    public TicketConst() {
    }
}
