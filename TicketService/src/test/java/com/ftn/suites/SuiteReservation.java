package com.ftn.suites;

import com.ftn.controllers.ReservationControllerTest;
import com.ftn.services.reservation.ReservationServiceIntegrationTest;
import com.ftn.services.reservation.ReservationServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ReservationServiceUnitTest.class,
        ReservationServiceIntegrationTest.class,
        ReservationControllerTest.class
})
public class SuiteReservation {
}
