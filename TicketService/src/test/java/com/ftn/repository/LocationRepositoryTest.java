package com.ftn.repository;

import com.ftn.constants.LocationConst;
import com.ftn.model.Location;
import com.ftn.project.TicketServiceApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
public class LocationRepositoryTest {

    @Autowired
    LocationRepository locationRepository;

    @Before
    public void init() {
        Location location = new Location("Spens", null, Collections.emptySet(), Collections.emptySet());
        locationRepository.save(location);
    }

    @After
    public void destroy() {
        locationRepository.deleteAll();
    }

    @Test
    @Transactional
    public void whenNameExist_thenReturnLocation() {
        Location l = locationRepository.findByLocationName(LocationConst.REAL_LOCATION_NAME);
        assertNotNull(l);
        assertEquals(LocationConst.REAL_LOCATION_NAME, l.getLocationName());
    }

    @Test
    @Transactional
    public void whenFindByNameReturnEmpty() {
        Location location = locationRepository.findByLocationName(LocationConst.BAD_LOCATION_NAME);
        assertNull(location);
    }
}
