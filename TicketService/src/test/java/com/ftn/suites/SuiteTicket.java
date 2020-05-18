package com.ftn.suites;

import com.ftn.controllers.TicketControllerTest;
import com.ftn.services.ticket.TicketServiceIntegrationTest;
import com.ftn.services.ticket.TicketServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TicketServiceUnitTest.class,
        TicketServiceIntegrationTest.class,
        TicketControllerTest.class
})
public class SuiteTicket {
}
