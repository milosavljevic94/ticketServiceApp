package com.ftn.repository.suit;


import com.ftn.repository.LocationRepositoryTest;
import com.ftn.repository.UserRepositoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocationRepositoryTest.class,
        UserRepositoryTest.class
})
public class RepositoryTestSuit {
}
