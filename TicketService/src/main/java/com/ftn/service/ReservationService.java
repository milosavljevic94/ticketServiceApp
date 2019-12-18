package com.ftn.service;


import com.ftn.dtos.ReservationDto;
import com.ftn.model.Reservation;
import com.ftn.model.Ticket;
import com.ftn.model.User;
import com.ftn.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TicketService ticketService;

    @Autowired
    UserService userService;

    public List<Reservation> finfAllReservation(){
        return reservationRepository.findAll();
    }

    public Reservation findOneReservation(Long id){
        return reservationRepository.findById(id).orElse(null);
    }

    public void addReservation(Reservation r){
        reservationRepository.save(r);
    }

    public void deleteReservation(Long id){
        reservationRepository.deleteById(id);
    }

    public void deleteAll(){
        reservationRepository.deleteAll();
    }

    public Boolean ifExist(Long id){
        return reservationRepository.existsById(id);
    }

    public ReservationDto mapToDTO(Reservation reservation){

        ReservationDto rDto = new ReservationDto(reservation);

        return rDto;
    }

    public List<ReservationDto> allToDto(){

        List<Reservation> reservations = finfAllReservation();
        List<ReservationDto> rdto = new ArrayList<>();

        for (Reservation r : reservations) {
            rdto.add(mapToDTO(r));
        }
        return rdto;
    }

    public Reservation mapFromDto(ReservationDto reservationDto){

        Reservation r = new Reservation();
        r.setId(reservationDto.getId());
        r.setActive(reservationDto.getActive());
        r.setExpDays(reservationDto.getExpDays());

        if(ticketService.findOneTicket(reservationDto.getTicket().getId()) != null){
            r.setTicket(ticketService.findOneTicket(reservationDto.getTicket().getId()));
        }else {

            Ticket t = new Ticket();
            t.setId(reservationDto.getTicket().getId());
            t.setRowNum(reservationDto.getTicket().getRowNum());
            t.setSeatNum(reservationDto.getTicket().getSeatNum());
            r.setTicket(t);
        }
        if(userService.findOneUser(reservationDto.getUser().getId()) != null) {
            r.setUser(userService.findOneUser(reservationDto.getUser().getId()));
        }else{
            /* Just register user can reserve ticket. */
            System.out.println("User not find.");
        }

        return r;
    }
}
