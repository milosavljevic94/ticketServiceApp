package com.ftn.repository;

import com.ftn.constants.LocationConst;
import com.ftn.model.Location;
import com.ftn.project.TicketServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class LocationRepositoryTest {

    @Autowired
    LocationRepository locationRepository;


    @Test
    public void whenNameExist_thenReturnLocation() {
        Location l = locationRepository.findByLocationName(LocationConst.REAL_LOCATION_NAME);

        assertNotNull(l);
        assertEquals(LocationConst.REAL_LOCATION_NAME, l.getLocationName());
    }

    @Test
    public void whenFindByNameReturnEmpty() {
        Location location = locationRepository.findByLocationName(LocationConst.BAD_LOCATION_NAME);
        assertNull(location);
    }
}
