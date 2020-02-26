package com.ftn.services.location;


import com.ftn.constants.AddressConst;
import com.ftn.constants.LocationConst;
import com.ftn.dtos.AddressDto;
import com.ftn.dtos.LocationDto;
import com.ftn.dtos.ManifestationInfoDto;
import com.ftn.dtos.SectorDto;
import com.ftn.enums.ManifestationCategory;
import com.ftn.exceptions.EntityAlreadyExistException;
import com.ftn.exceptions.LocationNotFoundException;
import com.ftn.model.Address;
import com.ftn.model.Location;
import com.ftn.model.Manifestation;
import com.ftn.model.Sector;
import com.ftn.repository.AddressRepository;
import com.ftn.repository.LocationRepository;
import com.ftn.repository.SectorRepository;
import com.ftn.service.AddressService;
import com.ftn.service.LocationService;
import com.ftn.service.SectorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceUnitTest {

    @InjectMocks
    private LocationService locationService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private SectorService sectorService;

    @Mock
    private SectorRepository sectorRepository;

    @Before
    public void setUp(){

        Address address = new Address("DrzavaTest",  "cityTest", "streetTest", "11b", 11.22, 33.55);
        address.setId(11L);

        Location location1 = new Location(LocationConst.REAL_LOCATION_NAME, address, new HashSet<>(), new HashSet<>());
        Location location2 = new Location();

        location1.setId(LocationConst.REAL_LOCATION_ID);

        location2.setId(LocationConst.LOCATION_MOCK_ID2);

        List<Location> locations = new ArrayList<>();

        locations.add(location1);
        locations.add(location2);

        Manifestation m1 = new Manifestation("testMan1", "descriptonTest", LocalDateTime.now(), LocalDateTime.of(2020,03,01,11,22,11), ManifestationCategory.CONCERT, new HashSet<>(), location1);

        location1.getManifestations().add(m1);

        when(locationRepository.findById(LocationConst.REAL_LOCATION_ID)).thenReturn(Optional.of(location1));
        when(locationRepository.findById(LocationConst.LOCATION_WRONG_ID)).thenThrow(LocationNotFoundException.class);
        when(locationRepository.findAll()).thenReturn(locations);
        when(locationRepository.findByLocationName(LocationConst.REAL_LOCATION_NAME)).thenReturn(location1);
        when(addressRepository.save(any(Address.class))).thenReturn(new Address());

    }

    @Test
    public void findAllLocationTest_thenReturnLocationList(){
        List<Location> list = locationService.finfAllLocation();
        assertEquals(2, list.size());
    }

    @Test
    public void findOneLocationExistTest_thenReturnLocation(){
        Long id = LocationConst.REAL_LOCATION_ID;
        Location l = locationService.findOneLocation(id);
        assertNotNull(l);
        assertEquals(id, l.getId());
    }

    @Test(expected = LocationNotFoundException.class)
    public void findOneLocationNotExistTest_thenThrowException(){
        Long id = LocationConst.LOCATION_WRONG_ID;
        Location l = locationService.findOneLocation(id);
    }

    @Test(expected = EntityAlreadyExistException.class)
    public void addLocationAndAddressNameAlreadyExistTest_thenThrowExeption(){

        LocationDto locationDtoExistingName = LocationConst.newLocationDto();
        locationDtoExistingName.setLocationName(LocationConst.REAL_LOCATION_NAME);

        Location locationTest = locationService.addLocationAndAddress(locationDtoExistingName);
    }

    @Test
    public void addLocationAndAddressSuccessTest_thenreturnLocation(){

        LocationDto locationDtoTest = LocationConst.newLocationDtoToAdd();
        when(locationRepository.save(any(Location.class))).thenAnswer(returnsFirstArg());
        when(addressService.mapFromDto(locationDtoTest.getAddress())).thenReturn(new Address(locationDtoTest.getAddress()));
        Location location = locationService.addLocationAndAddress(locationDtoTest);

        assertNotNull(location);
        assertEquals(locationDtoTest.getLocationName(), location.getLocationName());
        assertEquals(locationDtoTest.getAddress().getState(), location.getAddress().getState());
        assertEquals(locationDtoTest.getAddress().getCity(), location.getAddress().getCity());
        assertEquals(locationDtoTest.getAddress().getNumber(), location.getAddress().getNumber());
        verify(locationRepository, times(1)).save(location);
    }

    @Test(expected = LocationNotFoundException.class)
    public void addSectorToLocationNotExist_thenThrowException(){

        Location locationTest = locationService.addSectorToLocation(LocationConst.LOCATION_WRONG_ID, LocationConst.newSectorDtoTest());
    }

    @Test
    public void addSectorToLocationSuccess_thenReturnLocation(){

        Long idLocationTest = LocationConst.REAL_LOCATION_ID;
        SectorDto sectorDtoTest = LocationConst.newSectorDtoTest();
        sectorDtoTest.setSectorName("testSektor");


        when(sectorService.mapFromDto(sectorDtoTest)).thenReturn(new Sector(sectorDtoTest));
        when(sectorRepository.save(any(Sector.class))).thenAnswer(returnsFirstArg());

        Location locationtest = locationService.addSectorToLocation(idLocationTest, sectorDtoTest);

        assertNotNull(locationtest);
        verify(sectorRepository, times(1)).save(any(Sector.class));
        assertEquals(1, locationtest.getSectors().size());
        assertEquals(sectorDtoTest.getSectorName(), locationtest.getSectors().iterator().next().getSectorName());

    }

    @Test
    public void deleteLocationTest(){

        Location locationTest = LocationConst.newLocation();

        locationService.deleteLocation(locationTest.getId());

        verify(locationRepository, times(1)).deleteById(locationTest.getId());
    }

    @Test
    public void upldateLocationSuccessTest(){

        LocationDto locationDtoTest = LocationConst.newLocationDto();
        locationDtoTest.setId(LocationConst.REAL_LOCATION_ID);
        locationDtoTest.setLocationName(LocationConst.LOCATION_UPDATE_NAME);

        Location locationResult = locationService.updateLocation(locationDtoTest);

        assertNotNull(locationResult);
        assertEquals(locationDtoTest.getId(), locationResult.getId());
        assertEquals(locationDtoTest.getLocationName(), locationResult.getLocationName());
    }

    @Test(expected = LocationNotFoundException.class)
    public void updateAddressOfLocationNotExist_thenThrowException(){

        Location l = locationService.updateAddress(LocationConst.LOCATION_WRONG_ID, AddressConst.newAddressDto());
    }

    @Test
    public void updateAddressOfLocationSuccess(){

        Long idTest = LocationConst.REAL_LOCATION_ID;
        AddressDto addressDtoTest = AddressConst.newAddressDto();
        addressDtoTest.setState(AddressConst.ADDRESS_STATE);
        addressDtoTest.setCity(AddressConst.ADDRESS_CITY);

        Location locationResult = locationService.updateAddress(idTest, addressDtoTest);

        assertNotNull(locationResult);
        assertNotNull(locationResult.getAddress());
        assertEquals(addressDtoTest.getState(), locationResult.getAddress().getState());
        assertEquals(addressDtoTest.getCity(), locationResult.getAddress().getCity());
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(locationRepository, times(1)).save(locationResult);
    }

    @Test
    public void getManifestationsOfLocationSuccess(){

        List<ManifestationInfoDto> infoTest = locationService.getManifestationsOfLocation(LocationConst.REAL_LOCATION_ID);

        assertNotNull(infoTest);
        assertEquals(1, infoTest.size());
    }
}