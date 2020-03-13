package com.ftn.services.sector;

import com.ftn.constants.LocationConst;
import com.ftn.constants.SectorConst;
import com.ftn.dtos.SectorDto;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.exceptions.LocationNotFoundException;
import com.ftn.model.Sector;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.SectorRepository;
import com.ftn.service.SectorService;
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
public class SectorServiceIntegrationTest {

    @Autowired
    SectorService sectorService;

    @Autowired
    SectorRepository sectorRepository;

    @Test
    public void findAllSectorTest_thenReturnSectorList(){
        List<Sector> list = sectorService.finfAllSector();
        assertEquals(SectorConst.VALID_SIZE_SECTOR, list.size());
    }

    @Test
    public void findOneSectorExistTest_thenReturnSector(){

        Sector result = sectorService.findOneSector(SectorConst.OK_ID_SECTOR);

        assertNotNull(result);
        assertEquals(SectorConst.VALID_NAME, result.getSectorName());
        assertEquals(SectorConst.VALID_COLUMNS, result.getColumns());
        assertEquals(SectorConst.VALID_ROWS, result.getRows());
        assertEquals(SectorConst.SEATS_NUM, result.getSeatsNumber());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneLocationNotExistTest_thenThrowException(){

        Sector result = sectorService.findOneSector(SectorConst.BAD_ID_SECTOR);
    }

    @Test(expected = LocationNotFoundException.class)
    public void addSectorWrongLocationTest_thenThrowException(){

        SectorDto sectorDto = SectorConst.newDtoToAdd();
        sectorDto.setLocationId(LocationConst.NOT_VALID_LOC_ID);
        Sector sectorResult = sectorService.addSector(sectorDto);
    }

    @Test
    public void addSectorSuccessTest(){

        SectorDto dto = SectorConst.newDtoToAdd();
        dto.setLocationId(LocationConst.VALID_LOC_ID);

        int sizeBeforeAdd = sectorRepository.findAll().size();
        Sector sectorResult = sectorService.addSector(dto);
        int sizeAfterAdd = sectorRepository.findAll().size();

        assertNotNull(sectorResult);
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
        assertEquals(dto.getSectorName(), sectorResult.getSectorName());
        assertEquals(dto.getRows(), sectorResult.getRows());
        assertEquals(dto.getColumns(), sectorResult.getColumns());
        assertEquals(dto.getSeatsNumber(), sectorResult.getSeatsNumber());
    }

    @Test
    public void deleteSectorTest(){
        Sector sector = sectorService.addSector(SectorConst.newDtoToAdd());

        int sizeBeforeDel = sectorRepository.findAll().size();

        sectorService.deleteSector(SectorConst.ID_TO_DELETE);

        int sizeAfterDel = sectorRepository.findAll().size();

        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }

    @Test(expected = LocationNotFoundException.class)
    public void allSectorForLocationNotExist_thenThrowException(){

        sectorService.allSectorsForLocation(LocationConst.NOT_VALID_LOC_ID);
    }

    @Test
    public void allSectorForLocationSuccess(){

        List<SectorDto> result = sectorService.allSectorsForLocation(LocationConst.VALID_LOC_ID);

        assertFalse(result.isEmpty());
        assertEquals(4, result.size());
    }

    @Test(expected = LocationNotFoundException.class)
    public void updateSectorForLocationNotExist_thenThrowException(){

        SectorDto sectorDto = SectorConst.newDtoToAdd();
        sectorDto.setLocationId(LocationConst.NOT_VALID_LOC_ID);
        sectorService.updateSector(sectorDto);
    }


    @Test
    public void updateSectorSuccess(){

        SectorDto sectorDto = SectorConst.newDtoToAdd();
        sectorDto.setId(SectorConst.OK_ID_SECTOR);
        sectorDto.setLocationId(LocationConst.VALID_LOC_ID);
        Sector result = sectorService.updateSector(sectorDto);

        assertNotNull(result);
        assertEquals(sectorDto.getSectorName(), result.getSectorName());
    }
}
