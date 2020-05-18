package com.ftn.suites;

import com.ftn.controllers.UserControllerTest;
import com.ftn.repository.UserRepositoryTest;
import com.ftn.services.user.UserServiceIntegrationTest;
import com.ftn.services.user.UserServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserServiceUnitTest.class,
        UserRepositoryTest.class,
        UserServiceIntegrationTest.class,
        UserControllerTest.class
})
public class SuiteUser {
}
