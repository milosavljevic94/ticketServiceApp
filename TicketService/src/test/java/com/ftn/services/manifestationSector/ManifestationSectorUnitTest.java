package com.ftn.services.manifestationSector;

import com.ftn.enums.ManifestationCategory;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Manifestation;
import com.ftn.model.ManifestationDays;
import com.ftn.model.ManifestationSector;
import com.ftn.model.Sector;
import com.ftn.repository.ManifestationSectorRepository;
import com.ftn.service.ManifestationSectorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ManifestationSectorUnitTest {

    @InjectMocks
    ManifestationSectorService manifestationSectorService;

    @Mock
    ManifestationSectorRepository manifestationSectorRepository;


    @Before
    public void setUp(){

        Manifestation m1 = new Manifestation("testMan1", "descriptonTest", LocalDateTime.now(), LocalDateTime.of(2020,03,01,11,22,11), ManifestationCategory.CONCERT, new HashSet<>(), null);


        ManifestationDays manifestationDays = new ManifestationDays("dayTest", "desctest", LocalDateTime.now(), m1,
                new HashSet<>(), new HashSet<>());
        manifestationDays.setId(1L);

        Sector sector = new Sector("testSektor", 5, 10, null);
        sector.setId(1L);

        ManifestationSector ms = new ManifestationSector(1L,  manifestationDays, sector, 150.00);

        when(manifestationSectorRepository.findById(1L)).thenReturn(Optional.of(ms));
    }

    @Test
    public void getSectorPriceByIdSuccessTest(){

        Long OK_ID = 1L;
        ManifestationSector result = manifestationSectorService.getSectorPriceById(OK_ID);

/*
        are the correct sector and day most important.
*/
        assertNotNull(result);
        assertEquals("testSektor", result.getSector().getSectorName());
        assertEquals("dayTest", result.getManifestationDays().getName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getSectorPriceByIdNotFoundTest_thenThrowException(){

        Long BAD_ID = 2121L;
        ManifestationSector result = manifestationSectorService.getSectorPriceById(BAD_ID);
    }
}
