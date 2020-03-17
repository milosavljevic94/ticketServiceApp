package com.ftn.services.manifestationDay;

import com.ftn.constants.ManDaysConst;
import com.ftn.dtos.ManifestationDaysDto;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.ManifestationDays;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ManifestationDaysRepository;
import com.ftn.service.ManifestationDayService;
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
public class ManifestationDaysIntegrationTest {

    @Autowired
    ManifestationDayService manifestationDayService;

    @Autowired
    ManifestationDaysRepository daysRepository;

    @Test
    public void findAllDaysTest_thenReturnDayList(){
        List<ManifestationDays> list = manifestationDayService.finfAllManifestationDays();

        assertFalse(list.isEmpty());
        assertEquals(ManDaysConst.DAYS_SIZE, list.size());
    }

    @Test
    public void findOneDayExistTest_thenReturnDay(){

        ManifestationDays result = manifestationDayService.findOneManifestationDays(ManDaysConst.VALID_ID);

        assertNotNull(result);
        assertEquals(ManDaysConst.VALID_NAME, result.getName());
        assertEquals(ManDaysConst.VALID_DESC, result.getDescription());
        assertEquals(ManDaysConst.VALID_TIME, result.getStartTime());

    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneDayNotExistTest_thenThrowException(){

        ManifestationDays md = manifestationDayService.findOneManifestationDays(ManDaysConst.NOT_VALID_ID);
    }


    @Test
    public void addDaySuccessTest(){

        int sizeBeforeAdd = daysRepository.findAll().size();
        ManifestationDays mdToAdd = new ManifestationDays();
        manifestationDayService.addManifestationDays(mdToAdd);
        int sizeAfterAdd = daysRepository.findAll().size();
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
    }


    @Test
    public void deleteDaySuccessTest(){
        //daysRepository.save(ManDaysConst.newManDay());

        int sizeBeforeDel = daysRepository.findAll().size();
        manifestationDayService.deleteManifestationDays(4L);
        int sizeAfterDel = daysRepository.findAll().size();

        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }

    @Test
    public void updateDaySuccessTest(){

        ManifestationDaysDto dto = ManDaysConst.newDtoToUpdate();
        ManifestationDays result = manifestationDayService.updateManifestationDays(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateDayNotExist_thenThrowException(){

        ManifestationDaysDto notValidDto = new ManifestationDaysDto();
        notValidDto.setId(ManDaysConst.NOT_VALID_ID);

        ManifestationDays result = manifestationDayService.updateManifestationDays(notValidDto);

    }
}
