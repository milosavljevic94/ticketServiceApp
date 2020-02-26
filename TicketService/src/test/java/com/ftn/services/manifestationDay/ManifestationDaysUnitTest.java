package com.ftn.services.manifestationDay;


import com.ftn.dtos.ManifestationDaysDto;
import com.ftn.enums.ManifestationCategory;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Manifestation;
import com.ftn.model.ManifestationDays;
import com.ftn.repository.ManifestationDaysRepository;
import com.ftn.service.ManifestationDayService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManifestationDaysUnitTest {

    @InjectMocks
    ManifestationDayService manifestationDayService;

    @Mock
    ManifestationDaysRepository manifestationDaysRepository;

    @Before
    public void setUp(){

        Manifestation m1 = new Manifestation("testMan1", "descriptonTest", LocalDateTime.now(), LocalDateTime.of(2020,03,01,11,22,11), ManifestationCategory.CONCERT, new HashSet<>(), null);


        ManifestationDays manifestationDays = new ManifestationDays("dayTest", "desctest", LocalDateTime.now(), m1,
                new HashSet<>(), new HashSet<>());
        manifestationDays.setId(1L);

        List<ManifestationDays> daysList = new ArrayList<>();
        daysList.add(manifestationDays);

        m1.getManifestationDays().addAll(daysList);

        when(manifestationDaysRepository.findById(1L)).thenReturn(Optional.of(manifestationDays));
        when(manifestationDaysRepository.findAll()).thenReturn(daysList);

    }

    @Test
    public void findAllDaysTest_thenReturnDayList(){
        List<ManifestationDays> list = manifestationDayService.finfAllManifestationDays();

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    public void findOnedayExistTest_thenReturnDay(){

        Long ok_id = 1L;
        ManifestationDays m = manifestationDayService.findOneManifestationDays(ok_id);
        assertNotNull(m);
        assertEquals(ok_id, m.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneDayNotExistTest_thenThrowException(){
        Long BAD_ID = 222L;
        ManifestationDays md = manifestationDayService.findOneManifestationDays(BAD_ID);
    }

    @Test
    public void addDaySuccessTest(){

        ManifestationDays mdToAdd = new ManifestationDays();

        manifestationDayService.addManifestationDays(mdToAdd);

        verify(manifestationDaysRepository, times(1)).save(mdToAdd);
    }

    @Test
    public void deleteDaySuccessTest(){

        ManifestationDays mdToDelete = new ManifestationDays();
        mdToDelete.setId(1L);

        manifestationDayService.deleteManifestationDays(1L);

        verify(manifestationDaysRepository, times(1)).deleteById(mdToDelete.getId());
    }

    @Test
    public void updateDaySuccessTest(){
        ManifestationDaysDto daysDtoUpdate = new ManifestationDaysDto();
        daysDtoUpdate.setId(1L);
        daysDtoUpdate.setName("novoIme");

        ManifestationDays result = manifestationDayService.updateManifestationDays(daysDtoUpdate);

        assertNotNull(result);
        assertEquals(daysDtoUpdate.getName(), result.getName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateDayNotExist_thenThrowException(){

        ManifestationDaysDto daysDtoUpdate = new ManifestationDaysDto();
        daysDtoUpdate.setId(4341L);

        ManifestationDays result = manifestationDayService.updateManifestationDays(daysDtoUpdate);

    }
}
