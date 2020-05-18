package com.ftn.suites;

import com.ftn.controllers.SectorControllerTest;
import com.ftn.repository.SectorRepositoryTest;
import com.ftn.services.sector.SectorServiceIntegrationTest;
import com.ftn.services.sector.SectorServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SectorServiceUnitTest.class,
        SectorRepositoryTest.class,
        SectorServiceIntegrationTest.class,
        SectorControllerTest.class
})
public class SuiteSector {
}
