package com.ftn.service;

import com.ftn.dtos.ManifestationDaysDto;
import com.ftn.dtos.ManifestationDto;
import com.ftn.dtos.ManifestationSectorPriceDto;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.DateException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.exceptions.LocationNotFoundException;
import com.ftn.model.*;
import com.ftn.repository.LocationRepository;
import com.ftn.repository.ManifestationRepository;
import com.ftn.repository.ManifestationSectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ManifestationService {

    @Autowired
    ManifestationRepository manifestationRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ManifestationDayService manifestationDayService;

    @Autowired
    ManifestationSectorRepository manifestationSectorRepository;

    public List<Manifestation> finfAllManifestation(){
        return manifestationRepository.findAll();
    }

    public Manifestation findOneManifestation(Long id){
        try {
            return manifestationRepository.findById(id).orElse(null);
        }catch (NoSuchElementException e){
            throw new EntityNotFoundException("Manifestation with id : " + id + "not found.");
        }
    }

    public Optional<Manifestation> findOneManifestationOptional(Long id){
        return manifestationRepository.findById(id);
    }

    public Manifestation addManifestation(ManifestationDto mDto){

        Manifestation m = mapFromDto(mDto);

        LocalDateTime start = LocalDateTime.of(mDto.getStartTime().toLocalDate(), mDto.getStartTime().toLocalTime());

        LocalDateTime end = LocalDateTime.of(mDto.getEndTime().toLocalDate(), mDto.getEndTime().toLocalTime());

        if(start.isAfter(end)){
            throw new DateException("Start date is after end date, please insert correctly.");
        }

        m.setStartTime(start);

        m.setEndTime(end);

        Location l = locationRepository.findById(mDto.getLocationId()).orElseThrow(()-> new EntityNotFoundException(
                                                "Location with id: " + mDto.getLocationId() + "not found."));

        m.setLocation(l);

        manifestationRepository.save(m);

        /*
            Ovde moze biti if da se proveri koliko dana je trajanje manifestacije (start-end).
            Ako je u okviru jedng dana nista lista dana je prazna.
            Ako je vise dana onda se kreira lista dana i setuje u manifestaciji.
         */

        Long num = ChronoUnit.DAYS.between(start,end);
        int daysNumber = num.intValue();

        if(daysNumber == mDto.getManDaysDto().size()) {

            Set<ManifestationDays> mds = new HashSet<>();

            for (ManifestationDaysDto mdDto : mDto.getManDaysDto()) {
                ManifestationDays md = new ManifestationDays();
                md = manifestationDayService.mapFromDto(mdDto);
                md.setManifestation(findOneManifestation(m.getId()));
                mds.add(md);
                manifestationDayService.addManifestationDays(md);
            }
            m.setManifestationDays(mds);
        }else{
            throw new AplicationException("You have  "+daysNumber+"  days to insert!");
        }

        return m;
    }

    public void deleteManifestation(Long id){
        manifestationRepository.deleteById(id);
    }

    public void deleteAll(){
        manifestationRepository.deleteAll();
    }

    public Boolean ifExist(Long id){
        return manifestationRepository.existsById(id);
    }

    public Manifestation updateManifestation(ManifestationDto mdto) {


        Manifestation m = manifestationRepository.findById(mdto.getId()).orElseThrow(()-> new EntityNotFoundException(
                                            "Manifestation with id : "+mdto.getId()+" not found"));

        m.setName(mdto.getName());
        m.setDescription(mdto.getDescription());
        m.setManifestationCategory(mdto.getManifestationCategory());
        m.setStartTime(mdto.getStartTime());
        m.setEndTime(mdto.getEndTime());

        manifestationRepository.save(m);

        return m;
    }


    public ManifestationDto mapToDTO(Manifestation m){

        ManifestationDto md = new ManifestationDto(m);

        return md;
    }

    public List<ManifestationDto> allToDto() {

        List<Manifestation> m = finfAllManifestation();
        List<ManifestationDto> md = new ArrayList<>();

        for (Manifestation mm : m) {
            md.add(mapToDTO(mm));
        }
        return md;
    }


    public Manifestation mapFromDto(ManifestationDto md) {

        Manifestation m = new Manifestation();

        m.setName(md.getName());
        m.setDescription(md.getDescription());
        m.setManifestationCategory(md.getManifestationCategory());
        m.setStartTime(md.getStartTime());
        m.setEndTime(md.getEndTime());


        return m;
    }

    public Manifestation setPriceForSectorAndDay(Long id, ManifestationSectorPriceDto sectorPriceDto) {

        /*
        id manifestacije, pronaci manifestaciju sa tim id - jem.
        u objektu sectorPriceDto je smesten id sektora, id dana i cena.
        pomocu njega iz manifestacije pronaci dan i
        pronaci sektor ili preko lokacije u manifestaciji ili preko liste u danima.
        napraviti novi objekat Manifestation sector i u njega setovati sve, sacuvati ga u bazu
        i setovati u ostalim objektima gde treba.
         */

        Manifestation m = findOneManifestation(id);

        ManifestationDays md = null;

        for(ManifestationDays md1 : m.getManifestationDays()){
            if(md1.getId() == sectorPriceDto.getDayId()){
                md = md1;
            }
        }

        Sector s = null;

        for(Sector s1 : m.getLocation().getSectors()){
            if(s1.getId() == sectorPriceDto.getSectorId()){
                s = s1;
            }
        }

        ManifestationSector manSector = new ManifestationSector();

        manSector.setManifestationDays(md);
        manSector.setSector(s);
        manSector.setPrice(sectorPriceDto.getPrice());

        md.getManifestationSectors().add(manSector);

        //manifestationDayService.addManifestationDays(md);
        manifestationSectorRepository.save(manSector);

        return m;
    }

    public List<ManifestationSectorPriceDto> getPricesForManifestation(Long id) {

        Manifestation m = manifestationRepository.findById(id).orElseThrow(()-> new LocationNotFoundException(
        "Location with id : "+id+"not found"));

        List<ManifestationSectorPriceDto> prices = new ArrayList<>();
        for(ManifestationDays md : m.getManifestationDays()) {
            for(ManifestationSector s : md.getManifestationSectors()) {
                ManifestationSectorPriceDto price = new ManifestationSectorPriceDto();
                price.setDayId(md.getId());
                price.setSectorId(s.getSector().getId());
                price.setPrice(s.getPrice());
                prices.add(price);
            }
        }
        return prices;
    }
}
