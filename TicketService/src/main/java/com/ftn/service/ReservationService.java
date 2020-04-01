package com.ftn.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.dtos.ReservationDto;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.ManifestationSector;
import com.ftn.model.Reservation;
import com.ftn.model.Ticket;
import com.ftn.model.User;
import com.ftn.repository.ManifestationSectorRepository;
import com.ftn.repository.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TicketService ticketService;

    @Autowired
    UserService userService;
    
    @Autowired
    ManifestationDayService manifestationDayService;
    
    @Autowired
    SectorService manifestationSectorService;
    
    @Autowired
    ManifestationSectorRepository manifestationSectorRepository;

    public List<Reservation> finfAllReservation(){
        return reservationRepository.findAll();
    }

    public Reservation findOneReservation(Long id){
        return reservationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(
                "Reservation with id : "+ id+" not found"));
    }

/*  Not currently used.
    public Optional<Reservation> findOneReservationOptional(Long id){
        return reservationRepository.findById(id);
    }
*/

    public void addReservation(Reservation r){
        reservationRepository.save(r);
    }

    public void deleteReservation(Long id){

        if(userService.getloggedInUser() == null){
            throw new AplicationException("You must log in first!");
        }

        User u = userService.getloggedInUser();

        List<Reservation> reservationsOfUser = new ArrayList<>();
        reservationsOfUser.addAll(u.getReservations());

        Reservation r = findOneReservation(id);

        if(reservationsOfUser.contains(r)) {

            ticketService.deleteTicket(r.getTicket().getId());
            reservationRepository.deleteById(id);
        }else{
            throw new AplicationException("Can't cancel other users reservations!");
        }
    }


    public void deleteAll(){
        reservationRepository.deleteAll();
    }

    /*  Not currently used.
    public Boolean ifExist(Long id){
        return reservationRepository.existsById(id);
    }
    */

    public List<ReservationDto> reservationOfUser(){

        List<Reservation> reservations = new ArrayList<>();
        List<ReservationDto> reservationDtos = new ArrayList<>();

        User u = userService.getloggedInUser();

        if (u == null) {
            throw new AplicationException("You must be logged in to get your reservations.");
        }

        reservations.addAll(u.getReservations());
        for (Reservation r : reservations) {
            reservationDtos.add(mapToDTO(r));
        }
        return reservationDtos;

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
        //r.setId(reservationDto.getId());
        r.setActive(reservationDto.getActive());
        r.setExpDays(reservationDto.getExpDays());

        /*if(ticketService.findOneTicket(reservationDto.getTicket().getId()) != null){
            r.setTicket(ticketService.findOneTicket(reservationDto.getTicket().getId()));
        }else {

            */Ticket t = new Ticket();
            //t.setId(reservationDto.getTicket().getId());
            
            t.setRowNum(reservationDto.getTicket().getWantedSeat().getRow());
            t.setSeatNum(reservationDto.getTicket().getWantedSeat().getRow());
            t.setPurchaseConfirmed(false);
            t.setUser(userService.findOneUser(reservationDto.getUser().getId()));	
            
            
            t.setPurchaseTime(LocalDateTime.now());
            t.setManifestationDays(this.manifestationDayService.findOneManifestationDays(reservationDto.getTicket().getDayId()));
            ManifestationSector ms = this.manifestationSectorRepository.findById(reservationDto.getTicket()
            		.getWantedSeat().getManSectorId())
                    .orElse(null);
            t.setManifestationSector(ms);
            r.setTicket(t);
        //}
        if(userService.findOneUser(reservationDto.getUser().getId()) != null) {
            r.setUser(userService.findOneUser(reservationDto.getUser().getId()));
        }else{
            /* Just register user can reserve ticket. */
            System.out.println("User not find.");
        }

        return r;
    }
}
