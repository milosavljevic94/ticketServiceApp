package com.ftn.suites;

import com.ftn.controllers.RoleControllerTest;
import com.ftn.services.role.RoleServiceIntegrationTest;
import com.ftn.services.role.RoleServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RoleServiceUnitTest.class,
        RoleServiceIntegrationTest.class,
        RoleControllerTest.class
})
public class SuiteRole {
}
