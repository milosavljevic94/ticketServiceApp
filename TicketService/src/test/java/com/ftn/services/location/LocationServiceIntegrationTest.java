package com.ftn.services.location;

import com.ftn.constants.LocationConst;
import com.ftn.dtos.LocationDto;
import com.ftn.exceptions.EntityAlreadyExistException;
import com.ftn.exceptions.LocationNotFoundException;
import com.ftn.model.Location;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.LocationRepository;
import com.ftn.service.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class LocationServiceIntegrationTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void findAllSuccessTest(){
        List<Location> result = locationService.finfAllLocation();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void findOneLocationExistTest_thenReturnLocation(){
        Long id = LocationConst.VALID_LOC_ID;
        Location result = locationService.findOneLocation(id);
        assertEquals(id, result.getId());
    }

    @Test(expected = LocationNotFoundException.class)
    public void findOneLocationNotExistTest_thenThrowException(){
        Long id = LocationConst.NOT_VALID_LOC_ID;
        Location result = locationService.findOneLocation(id);
    }

    @Test(expected = EntityAlreadyExistException.class)
    public void addLocationAlreadyExist_thenThrowException(){
        LocationDto locationDto = LocationConst.newLocationDtoToAdd();
        locationDto.setLocationName(LocationConst.REAL_LOCATION_NAME);
        Location result = locationService.addLocationAndAddress(locationDto);
    }

    @Test
    public void addLocationAndAddressSuccessTest(){

        int sizeBeforeAdd = locationRepository.findAll().size();

        LocationDto locationDto = LocationConst.newDtoToAdd();
        Location result = locationService.addLocationAndAddress(locationDto);

        int sizeAfterAdd = locationRepository.findAll().size();

        assertNotNull(result);
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
        assertEquals(locationDto.getLocationName(), result.getLocationName());
        assertEquals(locationDto.getAddress().getState(), result.getAddress().getState());
        assertEquals(locationDto.getAddress().getCity(), result.getAddress().getCity());
        assertEquals(locationDto.getAddress().getStreet(), result.getAddress().getStreet());
    }

    
}
