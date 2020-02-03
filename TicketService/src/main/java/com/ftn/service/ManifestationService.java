package com.ftn.service;

import com.ftn.dtos.ManifestationDaysDto;
import com.ftn.dtos.ManifestationDto;
import com.ftn.model.Location;
import com.ftn.model.Manifestation;
import com.ftn.model.ManifestationDays;
import com.ftn.repository.LocationRepository;
import com.ftn.repository.ManifestationDaysRepository;
import com.ftn.repository.ManifestationRepository;
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

    public List<Manifestation> finfAllManifestation(){
        return manifestationRepository.findAll();
    }

    public Manifestation findOneManifestation(Long id){
        return manifestationRepository.findById(id).orElse(null);
    }

    public Optional<Manifestation> findOneManifestationOptional(Long id){
        return manifestationRepository.findById(id);
    }

    public Manifestation addManifestation(ManifestationDto mDto){

        Manifestation m = mapFromDto(mDto);

        LocalDateTime start = LocalDateTime.of(mDto.getStartTime().toLocalDate(), mDto.getStartTime().toLocalTime());

        LocalDateTime end = LocalDateTime.of(mDto.getEndTime().toLocalDate(), mDto.getEndTime().toLocalTime());

        m.setStartTime(start);

        m.setEndTime(end);

        Location l = locationRepository.findById(mDto.getLocationId()).orElse(null);

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
                md.setManifestation(findOneManifestation(mDto.getId()));
                mds.add(md);
                manifestationDayService.addManifestationDays(md);
            }
            m.setManifestationDays(mds);
        }else{
            System.out.println("You have "+daysNumber+" days to insert!");
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

    public void updateManifestation(ManifestationDto mdto) {


        Manifestation manifestation = manifestationRepository.findById(mdto.getId()).orElse(null);

        manifestation.setName(mdto.getName());
        manifestation.setDescription(mdto.getDescription());
        manifestation.setManifestationCategory(mdto.getManifestationCategory());

        manifestationRepository.save(manifestation);
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
}
