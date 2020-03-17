package com.ftn.services.location;

import com.ftn.constants.AddressConst;
import com.ftn.constants.LocationConst;
import com.ftn.constants.SectorConst;
import com.ftn.dtos.AddressDto;
import com.ftn.dtos.LocationDto;
import com.ftn.dtos.ManifestationInfoDto;
import com.ftn.dtos.SectorDto;
import com.ftn.exceptions.EntityAlreadyExistException;
import com.ftn.exceptions.LocationNotFoundException;
import com.ftn.model.Location;
import com.ftn.model.Sector;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.LocationRepository;
import com.ftn.repository.SectorRepository;
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

    @Autowired
    private SectorRepository sectorRepository;

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

    @Test(expected = LocationNotFoundException.class)
    public void addSectorLocationNotExist_thenThrowException(){

        SectorDto sectorToAdd = SectorConst.newDtoToAdd();
        Location result = locationService.addSectorToLocation(LocationConst.NOT_VALID_LOC_ID, sectorToAdd);
    }

    @Test
    public void addSectorSuccessTest(){
        int sectorSizeBeforeAdd = locationRepository.findById(1L).get().getSectors().size();
        SectorDto sectorToAdd = SectorConst.newDtoToAdd();
        Location result = locationService.addSectorToLocation(LocationConst.VALID_LOC_ID, sectorToAdd);
        int sectorSizeAfterAdd = locationRepository.findById(1L).get().getSectors().size();
        Sector addedSector = sectorRepository.findBySectorName(sectorToAdd.getSectorName());

        assertNotNull(result);
        assertEquals(sectorSizeBeforeAdd+1, sectorSizeAfterAdd);
        assertEquals(sectorToAdd.getSectorName(), addedSector.getSectorName());
        assertEquals(sectorToAdd.getColumns(), addedSector.getColumns());
        assertEquals(sectorToAdd.getRows(), addedSector.getRows());
        assertEquals(sectorToAdd.getSeatsNumber(), addedSector.getSeatsNumber());

    }

    @Test
    public void deleteLocationSuccessTest(){

        Location location = locationService.addLocationAndAddress(LocationConst.newDtoToAdd());

        int sizeBeforeDelete = locationRepository.findAll().size();
        locationService.deleteLocation(3L);
        int sizeAfterDelete = locationRepository.findAll().size();

        assertEquals(sizeBeforeDelete-1, sizeAfterDelete);
    }

    @Test(expected = LocationNotFoundException.class)
    public void updateLocationNotValidId_thenThrowException(){
        LocationDto locationDto = new LocationDto();
        locationDto.setId(LocationConst.NOT_VALID_LOC_ID);
        locationDto.setLocationName("novo ime update");

        Location result = locationService.updateLocation(locationDto);
    }

    @Test
    public void updateLocationSuccessTest(){

        LocationDto locationDto = new LocationDto();
        locationDto.setId(LocationConst.VALID_LOC_ID);
        locationDto.setLocationName("novo ime update");

        Location result = locationService.updateLocation(locationDto);

        assertNotNull(result);
        assertEquals(locationDto.getLocationName(), result.getLocationName());
    }

    @Test(expected = LocationNotFoundException.class)
    public void updateLocationAddressNotValidID_thenThrowException(){

        AddressDto addressDto = AddressConst.newAddressDtoForUpdate();

        Location result = locationService.updateAddress(LocationConst.NOT_VALID_LOC_ID, addressDto);
    }

    @Test
    public void updateLocationAddressSuccessTest(){

        AddressDto addressDto = AddressConst.newAddressDtoForUpdate();

        Location result = locationService.updateAddress(LocationConst.VALID_LOC_ID, addressDto);

        assertNotNull(result);
        assertEquals(addressDto.getState(), result.getAddress().getState());
        assertEquals(addressDto.getCity(), result.getAddress().getCity());
        assertEquals(addressDto.getStreet(), result.getAddress().getStreet());
        assertEquals(addressDto.getNumber(), result.getAddress().getNumber());
        assertEquals(addressDto.getLatitude(), result.getAddress().getLatitude());
        assertEquals(addressDto.getLongitude(), result.getAddress().getLongitude());
    }

    @Test(expected = LocationNotFoundException.class)
    public void getManifestationOfLocationNotValidID_thenThrowException(){

        List<ManifestationInfoDto> result = locationService.getManifestationsOfLocation(LocationConst.NOT_VALID_LOC_ID);

    }

    @Test
    public void getManifestationOfLocationSuccessTest(){

        List<ManifestationInfoDto> result = locationService.getManifestationsOfLocation(LocationConst.VALID_LOC_ID);
        Location expected = locationRepository.findById(LocationConst.VALID_LOC_ID).get();

        assertFalse(result.isEmpty());
        assertEquals(expected.getManifestations().size(),result.size());
    }
}
