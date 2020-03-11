package com.ftn.services.manifestationSector;

import com.ftn.constants.ManSectorConst;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.ManifestationSector;
import com.ftn.project.TicketServiceApplication;
import com.ftn.service.ManifestationSectorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class ManifestationSectorIntegrationTest {

    @Autowired
    ManifestationSectorService manSectorService;

    @Test
    public void getSectorPriceByIdSuccessTest() {

        ManifestationSector result = manSectorService.getSectorPriceById(ManSectorConst.VALID_ID);

        // is day and sector are correct is most important.

        assertNotNull(result);
        assertEquals(ManSectorConst.VALID_SECTOR_NAME, result.getSector().getSectorName());
        assertEquals(ManSectorConst.VALID_DAY_NAME, result.getManifestationDays().getName());
        assertEquals(ManSectorConst.VALID_PRICE, 250.00, 0);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getSectorPriceByIdNotFoundTest_thenThrowException(){

        ManifestationSector result = manSectorService.getSectorPriceById(ManSectorConst.NOT_VALID_ID);
    }
}
