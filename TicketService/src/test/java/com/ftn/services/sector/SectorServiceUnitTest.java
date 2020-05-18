package com.ftn.services.sector;

import com.ftn.constants.LocationConst;
import com.ftn.constants.SectorConst;
import com.ftn.dtos.SectorDto;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.exceptions.LocationNotFoundException;
import com.ftn.model.*;
import com.ftn.repository.ManifestationSectorRepository;
import com.ftn.repository.SectorRepository;
import com.ftn.service.LocationService;
import com.ftn.service.SectorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SectorServiceUnitTest {

    @InjectMocks
    SectorService sectorService;

    @Mock
    SectorRepository sectorRepository;

    @Mock
    LocationService locationService;

    @Mock
    ManifestationSectorRepository manSectorRepo;

    @Before
    public void setUp(){

        Address address = new Address("DrzavaTest",  "cityTest", "streetTest", "11b", 11.22, 33.55);
        address.setId(11L);
        Location location1 = new Location(LocationConst.REAL_LOCATION_NAME, address, new HashSet<>(), new HashSet<>());
        location1.setId(11L);

        Sector sector = new Sector("testSector", 5, 10, location1);
        sector.setId(1L);

        List<Sector> sectorList = new ArrayList<>();
        sectorList.add(sector);

        location1.getSectors().addAll(sectorList);

        ManifestationSector ms1 = new ManifestationSector(1L, new ManifestationDays(), sector, 120.0);
        ManifestationSector ms2 = new ManifestationSector(2L, new ManifestationDays(), sector, 150.0);
        List<ManifestationSector> manSectorList = new ArrayList<>();
        manSectorList.add(ms1);
        manSectorList.add(ms2);

        when(sectorRepository.findById(SectorConst.OK_ID_SECTOR)).thenReturn(Optional.of(sector));
        when(sectorRepository.findAll()).thenReturn(sectorList);
        when(locationService.findOneLocation(11L)).thenReturn(location1);
        when(manSectorRepo.findAll()).thenReturn(manSectorList);
    }


    @Test
    public void findAllSectorTest_thenReturnSectorList(){
        List<Sector> list = sectorService.finfAllSector();
        assertEquals(1, list.size());
    }

    @Test
    public void findOneSectorExistTest_thenReturnSector(){
        Long id = SectorConst.OK_ID_SECTOR;
        Sector s = sectorService.findOneSector(id);
        assertNotNull(s);
        assertEquals(id, s.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneLocationNotExistTest_thenThrowException(){
        Long id = SectorConst.BAD_ID_SECTOR;
        Sector sector = sectorService.findOneSector(id);
    }

    @Test(expected = LocationNotFoundException.class)
    public void addSectorWrongLocationTest_thenThrowException(){

        SectorDto sectorDto = SectorConst.newDtoToAdd();
        sectorDto.setLocationId(1221L);

        Sector sectorResult = sectorService.addSector(sectorDto);

    }

    @Test
    public void addSectorSuccessTest(){

        SectorDto sectorDto = SectorConst.newDtoToAdd();
        sectorDto.setLocationId(11L);

        Sector sectorResult = sectorService.addSector(sectorDto);

        assertNotNull(sectorResult);
        assertEquals("noviSektorTest", sectorResult.getSectorName());
        verify(sectorRepository, times(1)).save(sectorResult);
    }

    @Test
    public void deleteSectorTest(){

        sectorService.deleteSector(1L);

        verify(sectorRepository, times(1)).deleteById(1L);
    }

    @Test(expected = LocationNotFoundException.class)
    public void allSectorForLocationNotExist_thenThrowException(){

        sectorService.allSectorsForLocation(7676L);
    }

    @Test
    public void allSectorForLocationSuccess(){

        List<SectorDto> listRes = sectorService.allSectorsForLocation(11L);

        assertFalse(listRes.isEmpty());
        assertEquals(1, listRes.size());
    }

    @Test(expected = LocationNotFoundException.class)
    public void updateSectorForLocationNotExist_thenThrowException(){

        SectorDto sectorDto = SectorConst.newDtoToAdd();
        sectorDto.setLocationId(222L);
        sectorService.updateSector(sectorDto);
    }


    @Test
    public void updateSectorSuccess(){

        SectorDto sectorDto = SectorConst.newDtoToAdd();
        sectorDto.setId(SectorConst.OK_ID_SECTOR);
        sectorDto.setLocationId(11L);
        Sector result = sectorService.updateSector(sectorDto);

        assertNotNull(result);
        assertEquals(sectorDto.getSectorName(), result.getSectorName());
    }
}
