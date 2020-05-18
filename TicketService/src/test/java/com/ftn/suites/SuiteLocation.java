package com.ftn.suites;

import com.ftn.controllers.LocationControllerTest;
import com.ftn.repository.LocationRepositoryTest;
import com.ftn.services.location.LocationServiceIntegrationTest;
import com.ftn.services.location.LocationServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocationServiceUnitTest.class,
        LocationRepositoryTest.class,
        LocationServiceIntegrationTest.class,
        LocationControllerTest.class
})
public class SuiteLocation {
}
