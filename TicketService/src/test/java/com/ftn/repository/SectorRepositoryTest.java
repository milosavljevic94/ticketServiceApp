package com.ftn.repository;

import com.ftn.constants.SectorConst;
import com.ftn.model.Sector;
import com.ftn.project.TicketServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class SectorRepositoryTest {

    @Autowired
    SectorRepository sectorRepository;

    @Test
    public void whenNameIsValid_thenReturnSector() {
        Sector s = sectorRepository.findBySectorName(SectorConst.VALID_NAME);

        assertNotNull(s);
        assertEquals(SectorConst.VALID_NAME, s.getSectorName());
    }

    @Test
    public void whenNameNotValid_thenReturnNull() {
        Sector s = sectorRepository.findBySectorName("NOT_VALID_NAME");

        assertNull(s);
    }
}
