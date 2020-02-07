package com.ftn.controller;


import com.ftn.dtos.BuyTicketDto;
import com.ftn.dtos.TicketDto;
import com.ftn.model.Ticket;
import com.ftn.model.User;
import com.ftn.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable Long id) {

        return new ResponseEntity<>(new TicketDto(ticketService.findOneTicket(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/buyTicket")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketDto> buyTicketMakeNewTicket(@RequestBody BuyTicketDto ticketToBuy) {

        System.out.println("Principal u kontroleru : "+ SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Ticket t = ticketService.buyTicket(ticketToBuy);

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
}
