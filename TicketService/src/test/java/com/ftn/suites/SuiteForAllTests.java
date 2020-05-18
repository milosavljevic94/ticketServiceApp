package com.ftn.suites;

import com.ftn.controllers.*;
import com.ftn.repository.LocationRepositoryTest;
import com.ftn.repository.SectorRepositoryTest;
import com.ftn.repository.UserRepositoryTest;
import com.ftn.services.address.AddressServiceIntegrationTest;
import com.ftn.services.address.AddressServiceUnitTest;
import com.ftn.services.location.LocationServiceIntegrationTest;
import com.ftn.services.location.LocationServiceUnitTest;
import com.ftn.services.manifestation.ManifestationServiceIntegrationTest;
import com.ftn.services.manifestation.ManifestationServiceUnitTest;
import com.ftn.services.manifestationDay.ManifestationDaysIntegrationTest;
import com.ftn.services.manifestationDay.ManifestationDaysUnitTest;
import com.ftn.services.manifestationSector.ManifestationSectorIntegrationTest;
import com.ftn.services.manifestationSector.ManifestationSectorUnitTest;
import com.ftn.services.reservation.ReservationServiceIntegrationTest;
import com.ftn.services.reservation.ReservationServiceUnitTest;
import com.ftn.services.role.RoleServiceIntegrationTest;
import com.ftn.services.role.RoleServiceUnitTest;
import com.ftn.services.sector.SectorServiceIntegrationTest;
import com.ftn.services.sector.SectorServiceUnitTest;
import com.ftn.services.ticket.TicketServiceIntegrationTest;
import com.ftn.services.ticket.TicketServiceUnitTest;
import com.ftn.services.user.UserServiceIntegrationTest;
import com.ftn.services.user.UserServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AddressServiceIntegrationTest.class,
        AddressServiceUnitTest.class,
        AddressControllerTest.class,

        LocationServiceUnitTest.class,
        LocationRepositoryTest.class,
        LocationServiceIntegrationTest.class,
        LocationControllerTest.class,

        ManifestationServiceUnitTest.class,
        ManifestationServiceIntegrationTest.class,
        ManifestationControllerTest.class,

        ManifestationDaysUnitTest.class,
        ManifestationDaysIntegrationTest.class,

        ManifestationSectorUnitTest.class,
        ManifestationSectorIntegrationTest.class,

        ReservationServiceUnitTest.class,
        ReservationServiceIntegrationTest.class,
        ReservationControllerTest.class,

        RoleServiceUnitTest.class,
        RoleServiceIntegrationTest.class,
        RoleControllerTest.class,

        SectorServiceUnitTest.class,
        SectorRepositoryTest.class,
        SectorServiceIntegrationTest.class,
        SectorControllerTest.class,

        TicketServiceUnitTest.class,
        TicketServiceIntegrationTest.class,
        TicketControllerTest.class,

        UserServiceUnitTest.class,
        UserRepositoryTest.class,
        UserServiceIntegrationTest.class,
        UserControllerTest.class
})
public class SuiteForAllTests {
}
