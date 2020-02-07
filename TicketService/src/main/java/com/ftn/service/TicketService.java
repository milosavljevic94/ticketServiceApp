package com.ftn.service;

import com.ftn.dtos.BuyTicketDto;
import com.ftn.dtos.SeatWithPriceDto;
import com.ftn.dtos.TicketDto;
import com.ftn.exceptions.SectorIsFullException;
import com.ftn.model.*;
import com.ftn.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

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

    public List<Ticket> finfAllTickets(){
        return ticketRepository.findAll();
    }

    public Ticket findOneTicket(Long id){
        return ticketRepository.findById(id).orElse(null);
    }

    public void addTicket(Ticket t){
        ticketRepository.save(t);
    }

    public void updateTicket(TicketDto ticketDto){
        Ticket t = findOneTicket(ticketDto.getId());
        t.setRowNum(ticketDto.getRowNum());
        t.setSeatNum(ticketDto.getSeatNum());
        if(ticketDto.getReservation() != null) {
            t.setReservation(reservationService.findOneReservation(ticketDto.getReservation().getId()));
        }else{
            t.setReservation(null);
        }
        addTicket(t);
    }

    public void deleteTicket(Long id){
        ticketRepository.deleteById(id);
    }

    public void deleteAll(){
        ticketRepository.deleteAll();
    }

    public Boolean ifExist(Long id){
        return ticketRepository.existsById(id);
    }

    public TicketDto mapToDTO(Ticket ticket){

        TicketDto tDto = new TicketDto(ticket);

        return tDto;
    }

    private Ticket createNewTicket(SeatWithPriceDto seatPrice, ManifestationDays md){

        ManifestationSector ms = mSectorService.getSectorPriceById(seatPrice.getManSectorId());
        Sector s = ms.getSector();

        int numTicketForSectorAndDay = 0;
        List<Ticket> ticketsCheck = ticketRepository.findAll();
        if(!ticketsCheck.isEmpty()) {
            for (Ticket t : ticketsCheck) {
                if (t.getManifestationDays().getId() == md.getId() && t.getManifestationSector().getSector().getId() == s.getId()) {
                    numTicketForSectorAndDay++;
                }
            }


            if (numTicketForSectorAndDay + 1 > s.getSeatsNumber()) {
                throw new SectorIsFullException("Sector with id : " + s.getId() + "is full, try another sector.");
            }
        }

        /*
            If sector have free seats find logged user and save new ticket.
         */

        //Ovde mi teba user

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();

        User u = userService.findByUsername(username);

        Ticket t = new Ticket();
        t.setUser(u);
        t.setSeatNum(seatPrice.getSeatNumber());
        t.setRowNum(seatPrice.getRow());
        t.setReservation(null);
        t.setManifestationDays(md);
        t.setManifestationSector(ms);

        ticketRepository.save(t);

        return t;
    }

    public Ticket buyTicket(BuyTicketDto ticketToBuy){

        ManifestationDays md = manDayService.findOneManifestationDays(ticketToBuy.getDayId());

        Ticket t = createNewTicket(ticketToBuy.getWantedSeat(), md);

        return t;
    }

    public List<TicketDto> allToDto(){

        List<Ticket> tickets = finfAllTickets();
        List<TicketDto> tdto = new ArrayList<>();

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

}
