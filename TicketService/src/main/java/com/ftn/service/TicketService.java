package com.ftn.service;

import com.ftn.dtos.TicketDto;
import com.ftn.model.Reservation;
import com.ftn.model.Ticket;
import com.ftn.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
