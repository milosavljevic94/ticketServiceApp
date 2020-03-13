package com.ftn.constants;

import com.ftn.model.Reservation;

public class ReservationConst {

    public static int RES_SIZE = 3;

    public static Long VALID_ID = 1L;
    public static Long NOT_VALID_ID = 111L;
    public static Integer VALID_EXPIRATION = 10;

    public static int NUM_RESERVATION_VALID_USER = 2;

    public static Long OTHER_USER_RESERVATION = 3L;

    public static Reservation reservationToAdd(){

        Reservation reservation = new Reservation();
        //reservation.setTicket(new Ticket());
        reservation.setActive(true);
        //reservation.setUser(new User());
        reservation.setExpDays(10);

        return reservation;
    }

    public ReservationConst() {
    }
}
