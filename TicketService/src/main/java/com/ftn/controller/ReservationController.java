package com.ftn.controller;

import com.ftn.dtos.ReservationDto;
import com.ftn.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "api/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping(value = "/allReservation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationDto>> getAllReservation(){

        List<ReservationDto> reservationDtos = reservationService.allToDto();

        return new ResponseEntity<>(reservationDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/allUserReservation")
    public ResponseEntity<List<ReservationDto>> getAllReservationForUser(){

        List<ReservationDto> reservationDtos = reservationService.reservationOfUser();

        return new ResponseEntity<>(reservationDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Long id) {

        return new ResponseEntity<>(new ReservationDto(reservationService.findOneReservation(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/addReservation", consumes = "application/json")
    public ResponseEntity<ReservationDto> addReservation(@RequestBody ReservationDto reservationDto) {

        reservationService.addReservation(reservationService.mapFromDto(reservationDto));

        return new ResponseEntity<>(new ReservationDto(reservationService.mapFromDto(reservationDto)) , HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateReservation", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReservationDto> updateReservation(@RequestBody ReservationDto reservationDto){

        reservationService.addReservation(reservationService.mapFromDto(reservationDto));

        return new ResponseEntity<>(new ReservationDto(reservationService.mapFromDto(reservationDto)), HttpStatus.OK);
    }

    @DeleteMapping(value = "cancelReservation/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id){

        reservationService.deleteReservation(id);

        return new ResponseEntity<>("Reservation and ticket deleted successfully!",HttpStatus.OK);
    }

    //When manifestation is over, may you can delete all.
    @DeleteMapping(value = "deleteAllReservations")
    public ResponseEntity<?> deleteAllReservation(){

        reservationService.deleteAll();

        return new ResponseEntity<>("All reservation deleted successfully!",HttpStatus.OK);
    }
}
