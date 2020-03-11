package com.ftn.services.manifestation;

import com.ftn.constants.ManifestationConst;
import com.ftn.dtos.ManifestationDto;
import com.ftn.dtos.ManifestationSectorPriceDto;
import com.ftn.enums.ManifestationCategory;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.DateException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Manifestation;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.ManifestationRepository;
import com.ftn.service.ManifestationService;
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
public class ManifestationServiceIntegrationTest {

    @Autowired
    ManifestationService manifestationService;

    @Autowired
    ManifestationRepository manifestationRepository;

    @Test
    public void findAllManifestationTest_thenReturnManifestationList() {

        List<Manifestation> list = manifestationService.finfAllManifestation();
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
    }

    @Test
    public void findOneManifestationExistTest_thenReturnManifestation() {

        Manifestation mResult = manifestationService.findOneManifestation(ManifestationConst.OK_MAN_ID);
        assertNotNull(mResult);
        assertEquals("Koncert 1234", mResult.getName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneManifestationNotExistTest_thenThrowException() {
        Manifestation result = manifestationService.findOneManifestation(ManifestationConst.BAD_MAN_ID);
    }

    @Test(expected = DateException.class)
    public void addManifestationWrongDate_thenThrowException() {

        ManifestationDto dtoWrongDate = ManifestationConst.newDtoWrongDate();
        Manifestation result = manifestationService.addManifestation(dtoWrongDate);
    }

    @Test(expected = EntityNotFoundException.class)
    public void addManifestationWrongLocation_thenThrowException() {

        ManifestationDto dtoWrongLocation = ManifestationConst.newDtoWrongLocation();
        Manifestation result = manifestationService.addManifestation(dtoWrongLocation);
    }

    @Test(expected = AplicationException.class)
    public void addManifestationWrongDayNumber_thenThrowException() {

        ManifestationDto dtoWrongDayNumber = ManifestationConst.newDtoWrongDaysNumber();
        Manifestation result = manifestationService.addManifestation(dtoWrongDayNumber);
    }

    @Test
    public void addManifestationSuccessTest() {

        int sizeBeforeAdd = manifestationRepository.findAll().size();
        ManifestationDto dtoCorrect = ManifestationConst.newDto();
        Manifestation result = manifestationService.addManifestation(dtoCorrect);
        int sizeAfterAdd = manifestationRepository.findAll().size();


        assertNotNull(result);
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);

        assertFalse(result.getManifestationDays().isEmpty());
        assertEquals(ManifestationCategory.CONCERT, result.getManifestationCategory());
        assertEquals(dtoCorrect.getName(), result.getName());
        assertEquals(dtoCorrect.getStartTime(), result.getStartTime());
        assertEquals(dtoCorrect.getEndTime(), result.getEndTime());

    }

    @Test
    public void deleteManifestationSuccessTest() {

        int sizeBeforeDel = manifestationRepository.findAll().size();
        manifestationService.deleteManifestation(ManifestationConst.OK_MAN_ID);
        int sizeAfterDel = manifestationRepository.findAll().size();

        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateManifestationWrongId_thenThrowException() {

        Manifestation result = manifestationService
                .updateManifestation(ManifestationConst.newDtoForUpdateWrongId());
    }


    @Test(expected = DateException.class)
    public void updateManifestationWrongDate_thenThrowException() {
        Manifestation result = manifestationService
                .updateManifestation(ManifestationConst.newDtoForUpdateWrongDate());
    }

    @Test
    public void updateManifestationSuccessTest() {

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
    public void getPricesForManifestationWrongId_thenThrowException() {

        List<ManifestationSectorPriceDto> result = manifestationService
                .getPricesForManifestation(ManifestationConst.BAD_MAN_ID);
    }

    @Test
    public void getPricesForManifestationSuccessTest() {

        List<ManifestationSectorPriceDto> result = manifestationService
                .getPricesForManifestation(ManifestationConst.OK_MAN_ID);

        //number of prices is 12, because we have 3 days and 4 sectors in our example manifestation.

        assertFalse(result.isEmpty());
        assertEquals(ManifestationConst.MAN1_DAYS_NUM * ManifestationConst.MAN1_SECTOR_NUM, result.size());
    }

    //tests for set prices for sector and day.

    @Test(expected = EntityNotFoundException.class)
    public void setPricesForSectorAndDayWrongId_thenThrowException() {

        Manifestation result = manifestationService
                .setPriceForSectorAndDay(ManifestationConst.BAD_MAN_ID, ManifestationConst.newSectorPrice());
    }

    @Test(expected = AplicationException.class)
    public void setPriceForDayNotValid_thenThrowException() {
        ManifestationSectorPriceDto price = new ManifestationSectorPriceDto();
        price.setDayId(4L);
        Manifestation result = manifestationService
                .setPriceForSectorAndDay(ManifestationConst.OK_MAN_ID, price);
    }

    @Test(expected = AplicationException.class)
    public void setPriceForSectorNotValid_thenThrowException() {
        ManifestationSectorPriceDto price = new ManifestationSectorPriceDto();
        price.setSectorId(6L);
        Manifestation result = manifestationService
                .setPriceForSectorAndDay(ManifestationConst.OK_MAN_ID, price);
    }

    @Test
    public void setPricesForSectorAndDaySuccessTest() {

        Manifestation result = manifestationService
                .setPriceForSectorAndDay(ManifestationConst.OK_MAN_ID, ManifestationConst.newSectorPrice());

        assertNotNull(result);
        assertTrue(result.getLocation().getSectors().stream().anyMatch(s -> s.getSectorName().equals("sectorSever")));
        assertTrue(result.getManifestationDays().stream().anyMatch(md -> md.getName().equals("Dan1 man1")));
        assertTrue(result.getManifestationDays().stream().anyMatch(manifestationDays -> manifestationDays.getManifestationSectors().stream().anyMatch(manifestationSector -> manifestationSector.getPrice() == 200.0)));
    }
}
