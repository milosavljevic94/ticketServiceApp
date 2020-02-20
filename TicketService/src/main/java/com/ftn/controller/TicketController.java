package com.ftn.controller;


import com.ftn.dtos.BuyTicketDto;
import com.ftn.dtos.ReservationDto;
import com.ftn.dtos.TicketDto;
import com.ftn.dtos.TicketReportDto;
import com.ftn.model.Reservation;
import com.ftn.model.Ticket;
import com.ftn.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping(value = "/allTicket")
    public ResponseEntity<List<TicketDto>> getAllTicket(){

        List<TicketDto> ticketDtos = ticketService.allToDto();

        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/allUserTicket")
    public ResponseEntity<List<TicketDto>> getAllTicketOfUser(){

        List<TicketDto> ticketDtos = ticketService.ticketsOfUser();

        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable Long id) {

        return new ResponseEntity<>(new TicketDto(ticketService.findOneTicket(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/buyTicket")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TicketDto> buyTicketMakeNewTicket(@RequestBody BuyTicketDto ticketToBuy) {

        Ticket t = ticketService.buyTicket(ticketToBuy);

        return new ResponseEntity<>(new TicketDto(t), HttpStatus.OK);
    }

    @GetMapping(value = "/reserveTicket")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ReservationDto> reserveTicketMakeNewTicket(@RequestBody BuyTicketDto ticketToReserve) {

        Reservation r = ticketService.reserveTicket(ticketToReserve);

        return new ResponseEntity<>(new ReservationDto(r), HttpStatus.OK);
    }


    @GetMapping(value = "/buyReservedTicket/{idReservation}")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TicketDto> buyReservedTicket(@PathVariable Long idReservation) {

        Ticket t = ticketService.buyReservedTicket(idReservation);

        return new ResponseEntity<>(new TicketDto(t), HttpStatus.OK);
    }


    @PostMapping(value = "/addTicket", consumes = "application/json")
    public ResponseEntity<TicketDto> addTicket(@RequestBody TicketDto ticketDto) {

        ticketService.addTicket(ticketService.mapFromDto(ticketDto));

        return new ResponseEntity<>(new TicketDto(ticketService.mapFromDto(ticketDto)) , HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateTicket", consumes = "application/json")
    public ResponseEntity<TicketDto> updateTicket(@RequestBody TicketDto ticketDto){

        ticketService.updateTicket(ticketDto);

        return new ResponseEntity<>(new TicketDto(ticketService.mapFromDto(ticketDto)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id){

        ticketService.deleteTicket(id);

        return new ResponseEntity<>("Ticket deleted successfully!", HttpStatus.OK);
    }

    @PostMapping(value = "/reportDayLocation/{idLocation}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketReportDto> reportForDayOnLocation(@PathVariable Long idLocation, @RequestBody String date) {

        TicketReportDto report = ticketService.makeReportDayLocation(idLocation, date);

        return new ResponseEntity<TicketReportDto>(report, HttpStatus.OK);
    }

    @PostMapping(value = "/reportMonthLocation/{idLocation}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketReportDto> reportForMonthOnLocation(@PathVariable Long idLocation, @RequestBody String date) {

        TicketReportDto report = ticketService.makeReportMonthLocation(idLocation, date);

        return new ResponseEntity<TicketReportDto>(report, HttpStatus.OK);
    }

    @PostMapping(value = "/reportYearLocation/{idLocation}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketReportDto> reportForYearOnLocation(@PathVariable Long idLocation, @RequestBody String date) {

        TicketReportDto report = ticketService.makeReportYearLocation(idLocation, date);

        return new ResponseEntity<TicketReportDto>(report, HttpStatus.OK);
    }


    @PostMapping(value = "/reportDayManifestation/{idMan}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketReportDto> reportForDayOfManifestation(@PathVariable Long idMan, @RequestBody Long idDayManifestation) {

        TicketReportDto report = ticketService.makeReportDayManifestation(idMan, idDayManifestation);

        return new ResponseEntity<TicketReportDto>(report, HttpStatus.OK);
    }

    @PostMapping(value = "/reportManifestation/{idMan}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketReportDto> reportForWholeManifestation(@PathVariable Long idMan) {

        TicketReportDto report = ticketService.makeReportWholeManifestation(idMan);

        return new ResponseEntity<TicketReportDto>(report, HttpStatus.OK);
    }

}
