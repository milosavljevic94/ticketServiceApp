package com.ftn.repository;

import com.ftn.constants.LocationConst;
import com.ftn.model.Location;
import com.ftn.project.TicketServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
//@DataJpaTest
@SpringBootTest(classes = TicketServiceApplication.class)
public class LocationRepositoryTest {

    @Autowired
    LocationRepository locationRepository;

    @Test
    public void whenNameExist_thenReturnLocation(){
        System.out.println("Usao u test.");
        Location l = locationRepository.findByLocationName(LocationConst.REAL_LOCATION_NAME);
        System.out.println("Lokacija u testu: " +l.toString());
        assertNotNull(l);
        assertEquals(LocationConst.REAL_LOCATION_NAME, l.getLocationName());
    }

    @Test
    public void whenFindByNameReturnEmpty() {
        Location location = locationRepository.findByLocationName(LocationConst.BAD_LOCATION_NAME);
        assertNull(location);
    }
}
