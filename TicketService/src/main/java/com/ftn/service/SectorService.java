package com.ftn.service;

import com.ftn.dtos.SectorDto;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.exceptions.LocationNotFoundException;
import com.ftn.model.Location;
import com.ftn.model.Sector;
import com.ftn.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectorService {

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    LocationService locationService;


    public List<Sector> finfAllSector(){
        return sectorRepository.findAll();
    }

    public Sector findOneSector(Long id){
        return sectorRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(
                    "Sector with id : "+id+" not found."));
    }


    public Sector addSector(SectorDto sd) {

        Sector sector = new Sector();

        if(locationService.findOneLocation(sd.getLocationId()) == null){
            throw new LocationNotFoundException("Location with id : " +sd.getLocationId() +" not found.");
        }

        Location l = locationService.findOneLocation(sd.getLocationId());


            sector = mapFromDto(sd);
            sector.setLocation(l);

            l.getSectors().add(sector);

            sectorRepository.save(sector);

        return sector;
    }

    public void deleteSector(Long id){
        sectorRepository.deleteById(id);
    }

    public void deleteAll(){
        sectorRepository.deleteAll();
    }

    public List<SectorDto> allSectorsForLocation(Long idLocation){

        if(locationService.findOneLocation(idLocation) == null){
            throw new LocationNotFoundException("Location not exits, pick another location.");
        }

        Location l = locationService.findOneLocation(idLocation);


        ArrayList<Sector> sectors = new ArrayList<Sector>();

        sectors.addAll(l.getSectors());

        return allSectorToDto(sectors);

    }

    public Sector updateSector(SectorDto sd) {

        if(locationService.findOneLocation(sd.getLocationId()) == null){
            throw new LocationNotFoundException("Location not exits, pick another location.");
        }

        Location l = locationService.findOneLocation(sd.getLocationId());


            Sector s = findOneSector(sd.getId());

            s.setSectorName(sd.getSectorName());
            s.setSeatsNumber(sd.getSeatsNumber());
            s.setRows(sd.getRows());
            s.setColumns(sd.getColumns());

            sectorRepository.save(s);

            return s;
    }


    public SectorDto mapToDTO(Sector s){

        SectorDto sd = new SectorDto(s);

        return sd;
    }

    public List<SectorDto> allToDto() {

        List<Sector> sectors = finfAllSector();
        List<SectorDto> sectorDtos = new ArrayList<>();

        for (Sector s : sectors) {
            sectorDtos.add(mapToDTO(s));
        }
        return sectorDtos;
    }

    public List<SectorDto> allSectorToDto(List<Sector> sectors) {

        List<SectorDto> sectorDtos = new ArrayList<>();

        for (Sector s : sectors) {
            sectorDtos.add(mapToDTO(s));
        }
        return sectorDtos;
    }

    public Sector mapFromDto(SectorDto sd) {

        Sector s = new Sector();

        s.setId(sd.getId());
        s.setSectorName(sd.getSectorName());
        s.setColumns(sd.getColumns());
        s.setRows(sd.getRows());
        s.setSeatsNumber(sd.getRows()*sd.getColumns());

        return s;
    }
}
