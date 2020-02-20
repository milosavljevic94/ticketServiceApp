package com.ftn.services;

import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Ticket;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.TicketRepository;
import com.ftn.service.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class TicketServiceTest {

    @Autowired
    TicketService ticketsService;

    @MockBean
    TicketRepository ticketRepositoryMocked;


    @Before
    public void init() {

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        Mockito.when(ticketRepositoryMocked.findById(ticket.getId()));

        ArrayList<Ticket> tickets = new ArrayList<>();
        Ticket t1 = new Ticket();
        t1.setId(2L);
        Ticket t2 = new Ticket();
        t1.setId(3L);
        tickets.add(t1);
        tickets.add(t2);
        Mockito.when(ticketRepositoryMocked.findAll()).thenReturn(tickets);
    }

    @Test
    public void whenValidId_thenreturnTicket() {
        Long id = 1L;
        Ticket result = ticketsService.findOneTicket(id);

        assertEquals(id, result.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void whenNotValidId_thenThrowEntityNotFoundException() {
        Long id = 3L;
        Ticket foundTicket = ticketsService.findOneTicket(id);
    }

}
