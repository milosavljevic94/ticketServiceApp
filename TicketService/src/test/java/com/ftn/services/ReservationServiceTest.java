package com.ftn.services;

import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Reservation;
import com.ftn.model.Role;
import com.ftn.model.Ticket;
import com.ftn.model.User;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ReservationRepository;
import com.ftn.repository.RoleRepository;
import com.ftn.repository.UserRepository;
import com.ftn.service.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class ReservationServiceTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @MockBean
    ReservationRepository reservationRepositoryMocked;

    @Autowired
    ReservationService reservationService;


    //Test method for find all reservations.
    @Test
    public void getAllReservationsTest(){

        Ticket ticket1  = new Ticket();
        ticket1.setId(22L);
        ticket1.setRowNum(2);
        ticket1.setSeatNum(4);

        Ticket ticket2  = new Ticket();
        ticket2.setId(33L);
        ticket2.setRowNum(3);
        ticket2.setSeatNum(3);

        List<Reservation> reservations = new ArrayList<>();
        Reservation r1 = new Reservation();
        r1.setId(11L);
        r1.setActive(true);
        r1.setTicket(ticket1);
        r1.setExpDays(5);

        Reservation r2 = new Reservation();
        r2.setId(22L);
        r2.setActive(true);
        r2.setTicket(ticket2);
        r2.setExpDays(7);

        reservations.add(r1);
        reservations.add(r2);

        Mockito.when(reservationRepositoryMocked.findAll()).thenReturn(reservations);
        List<Reservation> result = reservationService.finfAllReservation();

        assertEquals(reservations.size(), result.size());
        assertEquals(r1.getId(),result.get(0).getId());
        assertEquals(r1.getTicket().getRowNum(),result.get(0).getTicket().getRowNum());
        assertEquals(r1.getExpDays(),result.get(0).getExpDays());


        assertEquals(r2.getId(),result.get(1).getId());
        assertEquals(r2.getTicket().getRowNum(),result.get(1).getTicket().getRowNum());
        assertEquals(r2.getExpDays(),result.get(1).getExpDays());


    }


    @Before
    public void init() {

        Role role = new Role("USER");
        roleRepository.save(role);

        User existingUser = new User("existing.email@gmail.com", "Existing", "User", "password", role, true, Collections.emptySet(), Collections.emptySet());
        existingUser.setUsername("existing_username");
        userRepository.save(existingUser);

        roleRepository.save(role);

        Reservation existingRes = new Reservation(existingUser, 5, true);
        reservationRepository.save(existingRes);


    }
    @Test
    public void getReservation_ByvalidIdTest() {

        Long validId = 1L;

        Reservation r = reservationService.findOneReservation(validId);

        assertEquals(r.getId(), validId);
        assertEquals(r.getExpDays().toString(), "5");
        assertEquals(r.getActive(), true);

    }


    @Test(expected = EntityNotFoundException.class)
    public void getReservation_ByvalidIdTest_throwEntitNotFoundException(){
        Long notValidId = 23L;
        reservationService.findOneReservation(notValidId);
    }

}
