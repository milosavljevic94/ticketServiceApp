package com.ftn.services.manifestation;


import com.ftn.constants.LocationConst;
import com.ftn.constants.ManifestationConst;
import com.ftn.dtos.ManifestationDto;
import com.ftn.dtos.ManifestationSectorPriceDto;
import com.ftn.enums.ManifestationCategory;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.DateException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.*;
import com.ftn.repository.LocationRepository;
import com.ftn.repository.ManifestationRepository;
import com.ftn.repository.ManifestationSectorRepository;
import com.ftn.service.ManifestationDayService;
import com.ftn.service.ManifestationService;
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

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ManifestationServiceUnitTest {

    @InjectMocks
    private ManifestationService manifestationService;

    @Mock
    private ManifestationRepository manifestationRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ManifestationDayService manifestationDayService;

    @Mock
    private ManifestationSectorRepository manifestationSectorRepository;

    @Before
    public void setUp(){

        //location with address
        Address address = new Address("DrzavaTest",  "cityTest", "streetTest", "11b", 11.22, 33.55);
        address.setId(11L);
        Location location1 = new Location(LocationConst.REAL_LOCATION_NAME, address, new HashSet<>(), new HashSet<>());
        location1.setId(ManifestationConst.LOCATION_ID);

        //manifestation
        Manifestation manifestationTest = new Manifestation("testMan", "descriptonTest", LocalDateTime.now(), LocalDateTime.of(2020,03,01,11,22,11), ManifestationCategory.CONCERT, new HashSet<>(), location1);
        manifestationTest.setId(ManifestationConst.OK_MAN_ID);

        //manifestation day with sector
        ManifestationDays manifestationDays = new ManifestationDays("dayTest", "desctest", LocalDateTime.now(), manifestationTest,
                new HashSet<>(), new HashSet<>());
        manifestationDays.setId(1L);

        Sector sector = new Sector("sectorTest", 5, 10, location1 );
        sector.setId(1L);
        location1.getSectors().add(sector);
        ManifestationSector manSector = new ManifestationSector(1L, manifestationDays, sector, 150.00);

        manifestationDays.getManifestationSectors().add(manSector);
        //set day to manifestation
        manifestationTest.getManifestationDays().add(manifestationDays);

        List<Manifestation> manifestations = new ArrayList<>();
        manifestations.add(manifestationTest);

        when(manifestationRepository.findById(ManifestationConst.OK_MAN_ID)).thenReturn(Optional.of(manifestationTest));
        when(manifestationRepository.findAll()).thenReturn(manifestations);
        when(locationRepository.findById(ManifestationConst.LOCATION_ID)).thenReturn(Optional.of(location1));
    }


    @Test
    public void findAllManifestationTest_thenReturnManifestationList(){
        List<Manifestation> list = manifestationService.finfAllManifestation();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    public void findOneManifestationExistTest_thenReturnManifestation(){
        Long id = ManifestationConst.OK_MAN_ID;
        Manifestation mResult = manifestationService.findOneManifestation(id);
        assertNotNull(mResult);
        assertEquals(id, mResult.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneManifestationNotExistTest_thenThrowException(){
        Long id = ManifestationConst.BAD_MAN_ID;
        Manifestation result = manifestationService.findOneManifestation(id);
    }

    @Test(expected = DateException.class)
    public void addManifestationWrongDate_thenThrowException(){

        ManifestationDto dtoWrongDate = ManifestationConst.newDtoWrongDate();
        Manifestation result = manifestationService.addManifestation(dtoWrongDate);
    }

    @Test(expected = EntityNotFoundException.class)
    public void addManifestationWrongLocation_thenThrowException(){

        ManifestationDto dtoWrongLocation = ManifestationConst.newDtoWrongLocation();
        Manifestation result = manifestationService.addManifestation(dtoWrongLocation);
    }

    @Test
    public void addManifestationSuccessTest(){

        Manifestation m = new Manifestation();
        m.setId(ManifestationConst.OK_MAN_ID);

        ManifestationDto dtoCorrect = ManifestationConst.newDto();
        doAnswer(invocation ->  {
            Object[] args = invocation.getArguments();
            ((Manifestation)args[0]).setId(ManifestationConst.OK_MAN_ID);
            return null;
        }).when(manifestationRepository).save(any(Manifestation.class));
        Manifestation result = manifestationService.addManifestation(dtoCorrect);

        assertNotNull(result);
        assertFalse(result.getManifestationDays().isEmpty());
        assertEquals( ManifestationCategory.CONCERT, result.getManifestationCategory());
        assertEquals(ManifestationConst.OK_MAN_ID, result.getId());
        assertEquals(dtoCorrect.getName(), result.getName());
    }

    @Test
    public void deleteManifestationSuccessTest(){

        manifestationService.deleteManifestation(ManifestationConst.OK_MAN_ID);

        verify(manifestationRepository, times(1)).deleteById(ManifestationConst.OK_MAN_ID);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateManifestationWrongId_thenThrowException(){

        Manifestation result = manifestationService
                                .updateManifestation(ManifestationConst.newDtoForUpdateWrongId());
    }

    @Test(expected = DateException.class)
    public void updateManifestationWrongDate_thenThrowException(){
        Manifestation result = manifestationService
                               .updateManifestation(ManifestationConst.newDtoForUpdateWrongDate());
    }

    @Test
    public void updateManifestationSuccessTest(){

        ManifestationDto okDto = ManifestationConst.newDtoForUpdate();
        Manifestation result = manifestationService
                .updateManifestation(okDto);

        assertNotNull(result);
        assertEquals(okDto.getName(), result.getName());
        assertEquals(okDto.getDescription(), result.getDescription());
        assertEquals(okDto.getManifestationCategory(), result.getManifestationCategory());
        assertEquals(okDto.getStartTime(), result.getStartTime());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getPricesForManifestationWrongId_thenThrowException(){

        List<ManifestationSectorPriceDto> result = manifestationService
                .getPricesForManifestation(ManifestationConst.BAD_MAN_ID);
    }

    @Test
    public void getPricesForManifestationSuccessTest(){

        List<ManifestationSectorPriceDto> result = manifestationService
                .getPricesForManifestation(ManifestationConst.OK_MAN_ID);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(Optional.of(1L).get(), result.iterator().next().getDayId());
        assertEquals(Optional.of(1L).get(), result.iterator().next().getSectorId());
        assertEquals(Optional.of(150.00).get(), result.iterator().next().getPrice());
    }

    @Test(expected = EntityNotFoundException.class)
    public void setPricesForSectorAndDayWrongId_thenThrowException(){

        Manifestation result = manifestationService
                .setPriceForSectorAndDay(ManifestationConst.BAD_MAN_ID, ManifestationConst.newSectorPrice());
    }

    @Test(expected = AplicationException.class)
    public void setPriceForDayNotValid_thenThrowException(){
        ManifestationSectorPriceDto price = new ManifestationSectorPriceDto();
        price.setDayId(4L);
        Manifestation result = manifestationService
                .setPriceForSectorAndDay(ManifestationConst.OK_MAN_ID, price);
    }

    @Test(expected = AplicationException.class)
    public void setPriceForSectorNotValid_thenThrowException(){
        ManifestationSectorPriceDto price = new ManifestationSectorPriceDto();
        price.setSectorId(6L);
        Manifestation result = manifestationService
                .setPriceForSectorAndDay(ManifestationConst.OK_MAN_ID, price);
    }

    @Test
    public void setPricesForSectorAndDaySuccessTest(){

        when(manifestationSectorRepository.save(any(ManifestationSector.class))).thenAnswer(returnsFirstArg());
        Manifestation result = manifestationService
                .setPriceForSectorAndDay(ManifestationConst.OK_MAN_ID, ManifestationConst.newSectorPrice());

        System.out.println("Result u testu : "+ result.toString());

        assertNotNull(result);
        assertTrue( result.getLocation().getSectors().iterator().next().getSectorName().contains("sectorTest"));
        assertTrue( result.getManifestationDays().iterator().next().getName().contains("dayTest"));
        assertTrue(result.getManifestationDays().iterator().next().getManifestationSectors().stream().anyMatch(p -> p.getPrice().equals(200.0)));
        verify(manifestationSectorRepository, times(1)).save(any(ManifestationSector.class));
    }
}
