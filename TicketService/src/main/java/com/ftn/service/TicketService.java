package com.ftn.service;

import com.ftn.constants.Restrictions;
import com.ftn.dtos.*;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.exceptions.SeatIsNotFreeException;
import com.ftn.exceptions.SectorIsFullException;
import com.ftn.model.*;
import com.ftn.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ReservationService reservationService;

    @Autowired
    UserService userService;

    @Autowired
    ManifestationSectorService mSectorService;

    @Autowired
    ManifestationDayService manDayService;

    @Autowired
    ManifestationService manifestationService;

    public List<Ticket> finfAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket findOneTicket(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket with id : " + id + "not found."));
    }

    public Ticket addTicket(Ticket t) {
        ticketRepository.save(t);
        return t;
    }

    /*
    Functionality marked as unnecessary and redundant.
     */
    public Ticket updateTicket(TicketDto ticketDto) {
        Ticket t = findOneTicket(ticketDto.getId());
        t.setRowNum(ticketDto.getRowNum());
        t.setSeatNum(ticketDto.getSeatNum());
        if (ticketDto.getReservation() != null) {
            t.setReservation(reservationService.findOneReservation(ticketDto.getReservation().getId()));
        } else {
            t.setReservation(null);
        }
        addTicket(t);
        return t;
    }

    public void deleteTicket(Long id) {

        if (userService.getloggedInUser() == null) {
            throw new AplicationException("You must be logged in to delete ticket!");
        }

        User u = userService.getloggedInUser();


        List<Ticket> usersTickets = new ArrayList<>();
        usersTickets.addAll(u.getTickets());

        Ticket ticketToDelete = findOneTicket(id);

        if (usersTickets.contains(ticketToDelete)) {
            u.getTickets().remove(ticketToDelete);
            userService.saveUser(u);
        } else {
            throw new AplicationException("Can not delete other user ticket!");
        }
    }

    public void deleteAll() {
        ticketRepository.deleteAll();
    }


    /*
    Checking if seat free for position, day and sector.
    Return value boolean.
    */
    public Boolean isSeatFree(int row, int seatNum, Long dayId, Long sectorId) {

        Boolean free = true;

        List<Ticket> tickets = finfAllTickets();

        for (Ticket t : tickets) {
            if (t.getManifestationDays().getId() == dayId && t.getManifestationSector().getSector().getId() == sectorId && t.getRowNum() == row && t.getSeatNum() == seatNum) {
                free = false;
                return free;
            } else {
                free = true;
            }
        }
        return free;
    }

    public Ticket buyTicket(BuyTicketDto ticketToBuy) {

        if (userService.getloggedInUser() == null) {
            throw new AplicationException("You must be logged in to buy ticket!");
        }

        User u = userService.getloggedInUser();

        Boolean forBuying = true;

        ManifestationDays md = manDayService.findOneManifestationDays(ticketToBuy.getDayId());

        Ticket t = createNewTicket(ticketToBuy.getWantedSeat(), md, forBuying);

        return t;
    }

    public Reservation reserveTicket(BuyTicketDto ticketToReserve) {

        if (userService.getloggedInUser() == null) {
            throw new AplicationException("You must be logged in to reserve ticket!");
        }

        User u = userService.getloggedInUser();

        Boolean forBuying = false;

        ManifestationDays md = manDayService.findOneManifestationDays(ticketToReserve.getDayId());

        LocalDateTime start = md.getStartTime();
        long days = ChronoUnit.DAYS.between(LocalDateTime.now(), start);
        int intDays = (int) days;

        if (intDays < Restrictions.DAYS_BEFORE_MANIFESTATION) {
            throw new AplicationException("You can't reserve ticket because manifestation starts for " + Restrictions.DAYS_BEFORE_MANIFESTATION + " days.");
        }


        Ticket t = createNewTicket(ticketToReserve.getWantedSeat(), md, forBuying);

        return t.getReservation();

    }


    private Ticket createNewTicket(SeatWithPriceDto seatPrice, ManifestationDays md, Boolean forBuying) {

        ManifestationSector ms = mSectorService.getSectorPriceByDayAndSector(md.getId(), seatPrice.getManSectorId());
        Sector s = ms.getSector();

        if (seatPrice.getRow() > s.getRows() || seatPrice.getSeatNumber() > s.getColumns()) {
            throw new AplicationException("This sector have " + s.getRows() + " rows and " + s.getColumns() + " columns. Please try again and insert correctly seat position!");
        }

        if (!isSeatFree(seatPrice.getRow(), seatPrice.getSeatNumber(), md.getId(), s.getId())) {
            throw new SeatIsNotFreeException("Seat in row: " + seatPrice.getRow() + " , and with number:  " + seatPrice.getSeatNumber() + "  is not free for this day in this sector." +
                    "Please try to insert another position, or another day or sector.");
        }

        int numTicketForSectorAndDay = 0;
        List<Ticket> ticketsCheck = ticketRepository.findAll();
        if (!ticketsCheck.isEmpty()) {
            for (Ticket t : ticketsCheck) {
                if (t.getManifestationDays().getId() == md.getId() && t.getManifestationSector().getSector().getId() == s.getId()) {
                    numTicketForSectorAndDay++;
                }
            }

            if (numTicketForSectorAndDay + 1 > s.getSeatsNumber()) {
                throw new SectorIsFullException("Sector with id : " + s.getId() + "is full, try another sector.");
            }
        }


        //If sector have free seats find logged user and save new ticket.

        User u = userService.getloggedInUser();

        /*
            If ticket is for buying directly, save ticket with buyer.
            If not for buying then is for reserving.
        */

        if (forBuying) {
            Ticket t = new Ticket();

            t.setUser(u);
            t.setSeatNum(seatPrice.getSeatNumber());
            t.setRowNum(seatPrice.getRow());
            t.setReservation(null);
            t.setManifestationDays(md);
            t.setManifestationSector(ms);
            t.setPurchaseConfirmed(true);
            t.setPurchaseTime(LocalDateTime.now());

            ticketRepository.save(t);

            return t;

        } else {
            Reservation reservation = makeReservationForTicket(seatPrice, md, ms, u);
            return reservation.getTicket();
        }
    }

    public Reservation makeReservationForTicket(SeatWithPriceDto seatPrice, ManifestationDays md, ManifestationSector ms, User u) {

        Reservation r = new Reservation();
        r.setActive(true);
        r.setExpDays(Restrictions.RESERVATION_DURATION);
        r.setUser(u);
        r.setTicket(null);

        reservationService.addReservation(r);


        Ticket tRes = new Ticket();

        tRes.setUser(u);
        tRes.setSeatNum(seatPrice.getSeatNumber());
        tRes.setPurchaseConfirmed(false);
        tRes.setPurchaseTime(LocalDateTime.now());
        tRes.setManifestationSector(ms);
        tRes.setManifestationDays(md);
        tRes.setRowNum(seatPrice.getRow());
        tRes.setReservation(r);


        ticketRepository.save(tRes);

        r.setTicket(tRes);

        reservationService.addReservation(r);

        return r;
    }

    public Ticket buyReservedTicket(Long idReservation) {

        if (userService.getloggedInUser() == null) {
            throw new AplicationException("You must be logged in to buy ticket!");
        }

        User u = userService.getloggedInUser();


        List<Reservation> reservations = new ArrayList<>();
        reservations.addAll(u.getReservations());

        Reservation r = reservationService.findOneReservation(idReservation);
        Ticket ticket = r.getTicket();

        if (reservations.contains(r)) {


            ticket.setUser(u);
            ticket.setPurchaseConfirmed(true);
            ticketRepository.save(ticket);

            reservationService.deleteReservation(r.getId());

        } else {
            throw new AplicationException(u.getUsername() + " you don't have reservation with id: " + idReservation);
        }
        return ticket;
    }


    public BuyTicketDto mapToDTO(Ticket ticket) {

        BuyTicketDto tDto = new BuyTicketDto(ticket);

        return tDto;
    }

    public List<BuyTicketDto> allToDto() {

        List<Ticket> tickets = finfAllTickets();
        List<BuyTicketDto> tdto = new ArrayList<>();

        for (Ticket t : tickets) {
            tdto.add(mapToDTO(t));
        }
        return tdto;
    }

    public Ticket mapFromDto(TicketDto ticketDto) {

        Ticket t = new Ticket();

        t.setId(ticketDto.getId());
        t.setRowNum(ticketDto.getRowNum());
        t.setSeatNum(ticketDto.getSeatNum());
        t.setPurchaseConfirmed(ticketDto.getPurchaseConfirmed());
        t.setPurchaseTime(ticketDto.getPurchaseTime());

        if (ticketDto.getReservation() != null) {
            if (reservationService.findOneReservation(ticketDto.getReservation().getId()) != null) {
                t.setReservation(reservationService.findOneReservation(ticketDto.getReservation().getId()));
            } else {
                Reservation r = new Reservation();
                r.setId(ticketDto.getReservation().getId());
                if (userService.findOneUser(ticketDto.getReservation().getUser().getId()) != null) {
                    r.setUser(userService.findOneUser(ticketDto.getReservation().getUser().getId()));
                } else {
                    System.out.println("User not found.");
                }
                r.setExpDays(ticketDto.getReservation().getExpDays());
                r.setActive(ticketDto.getReservation().getActive());
                t.setReservation(r);
            }
            return t;

        } else {
            t.setReservation(null);
            return t;
        }
    }


    public List<BuyTicketDto> ticketsOfUser() {

        List<Ticket> tickets = new ArrayList<>();
        List<BuyTicketDto> ticketDtos = new ArrayList<>();

        if (userService.getloggedInUser() == null) {
            throw new AplicationException("You must be logged in to buy ticket!");
        }

        User u = userService.getloggedInUser();

        tickets.addAll(u.getTickets());
        for (Ticket t : tickets) {
            ticketDtos.add(mapToDTO(t));
        }
        return ticketDtos;

    }

    public TicketReportDto makeReportDayLocation(Long idLocation, String date) {

        List<Ticket> allTickets = ticketRepository.findAll();
        double profit = 0.0;
        int soldTickets = 0;

        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(date, format);

            for (Ticket t : allTickets) {
                if (t.getManifestationDays().getManifestation().getLocation().getId() == idLocation &&
                        t.getPurchaseTime().toLocalDate().equals(dateTime)) {
                    profit = profit + t.getManifestationSector().getPrice();
                    soldTickets = soldTickets + 1;

                }
            }
        } catch (DateTimeParseException e) {
            throw new AplicationException("Please import date in format : yyyy-MM-dd, " + e);
        }

        return new TicketReportDto(profit, soldTickets);
    }

    public TicketReportDto makeReportMonthLocation(Long idLocation, String date) {

        List<Ticket> allTickets = ticketRepository.findAll();
        double profit = 0.0;
        int soldTickets = 0;

        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth dateTime = YearMonth.parse(date, format);

            for (Ticket t : allTickets) {
                if (t.getManifestationDays().getManifestation().getLocation().getId() == idLocation &&
                        YearMonth.from(t.getPurchaseTime()).equals(dateTime)) {
                    profit = profit + t.getManifestationSector().getPrice();
                    soldTickets = soldTickets + 1;
                }
            }
        } catch (DateTimeParseException e) {
            throw new AplicationException("Please import date in format : yyyy-MM, " + e);
        }

        return new TicketReportDto(profit, soldTickets);
    }

    public TicketReportDto makeReportYearLocation(Long idLocation, String date) {

        List<Ticket> allTickets = ticketRepository.findAll();
        double profit = 0.0;
        int soldTickets = 0;

        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy");
            Year dateTime = Year.parse(date, format);

            for (Ticket t : allTickets) {
                if (t.getManifestationDays().getManifestation().getLocation().getId() == idLocation &&
                        Year.from(t.getPurchaseTime()).equals(dateTime)) {
                    profit = profit + t.getManifestationSector().getPrice();
                    soldTickets = soldTickets + 1;
                }
            }
        } catch (DateTimeParseException e) {
            throw new AplicationException("Please import date in format : yyyy, " + e);
        }

        return new TicketReportDto(profit, soldTickets);
    }

    public TicketReportDto makeReportDayManifestation(Long idDayManifestation) {

        ManifestationDays md = manDayService.findOneManifestationDays(idDayManifestation);
        double profit = 0.0;
        int soldTickets = 0;

        for (Ticket t : md.getTickets()) {
            profit = profit + t.getManifestationSector().getPrice();
            soldTickets = soldTickets + 1;
        }

        return new TicketReportDto(profit, soldTickets);
    }

    public TicketReportDto makeReportWholeManifestation(Long idMan) {
        Set<ManifestationDays> manDays = manifestationService.findOneManifestation(idMan).getManifestationDays();
        double profit = 0.0;
        int soldTickets = 0;

        for (ManifestationDays md : manDays) {
            for (Ticket t : md.getTickets()) {
                profit = profit + t.getManifestationSector().getPrice();
                soldTickets = soldTickets + 1;
            }

        }
        return new TicketReportDto(profit, soldTickets);
    }

    public ManForTicketDto getManNameForTicket(Long idTicket) {
        Ticket t = ticketRepository.getOne(idTicket);

        return new ManForTicketDto(t.getManifestationDays().getManifestation().getName());
    }
}
