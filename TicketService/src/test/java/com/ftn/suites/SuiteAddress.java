package com.ftn.suites;

import com.ftn.controllers.AddressControllerTest;
import com.ftn.services.address.AddressServiceIntegrationTest;
import com.ftn.services.address.AddressServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AddressServiceIntegrationTest.class,
        AddressServiceUnitTest.class,
        AddressControllerTest.class
})
public class SuiteAddress {
}
