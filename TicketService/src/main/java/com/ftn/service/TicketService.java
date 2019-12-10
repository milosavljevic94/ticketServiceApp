package com.ftn.service;

import com.ftn.model.Ticket;
import com.ftn.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;


    public List<Ticket> finfAllTickets(){
        return ticketRepository.findAll();
    }

    public Ticket findOneTicket(Long id){
        return ticketRepository.findById(id).orElse(null);
    }

    public void addTicket(Ticket t){
        ticketRepository.save(t);
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
}
