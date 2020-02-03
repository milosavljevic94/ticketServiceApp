package com.ftn.service;

import com.ftn.dtos.ManifestationDaysDto;
import com.ftn.model.ManifestationDays;
import com.ftn.repository.ManifestationDaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManifestationDayService {

    @Autowired
    ManifestationDaysRepository manifestationDaysRepository;

    public List<ManifestationDays> finfAllManifestationDays(){
        return manifestationDaysRepository.findAll();
    }

    public ManifestationDays findOneManifestationDays(Long id){
        return manifestationDaysRepository.findById(id).orElse(null);
    }

    public Optional<ManifestationDays> findOneManifestationDaysOptional(Long id){
        return manifestationDaysRepository.findById(id);
    }

    public void addManifestationDays(ManifestationDays md){
        manifestationDaysRepository.save(md);
    }

    public void deleteManifestationDays(Long id){
        manifestationDaysRepository.deleteById(id);
    }

    public void deleteAll(){
        manifestationDaysRepository.deleteAll();
    }

    public Boolean ifExist(Long id){
        return manifestationDaysRepository.existsById(id);
    }

    public void updateManifestationDays(ManifestationDaysDto mdd) {

        ManifestationDays md = findOneManifestationDays(mdd.getId());

        md.setName(mdd.getName());
        md.setDescription(mdd.getDescription());

        manifestationDaysRepository.save(md);
    }


    public ManifestationDaysDto mapToDTO(ManifestationDays md){

        ManifestationDaysDto mdto = new ManifestationDaysDto(md);

        return mdto;
    }

    public List<ManifestationDaysDto> allToDto() {

        List<ManifestationDays> mds = finfAllManifestationDays();
        List<ManifestationDaysDto> mdtos = new ArrayList<>();

        for (ManifestationDays md : mds) {
            mdtos.add(mapToDTO(md));
        }
        return mdtos;
    }

    public ManifestationDays mapFromDto(ManifestationDaysDto mdto) {

        ManifestationDays md = new ManifestationDays();

        md.setName(mdto.getName());
        md.setDescription(mdto.getDescription());
        md.setStartTime(mdto.getStartTime());

        return md;
    }
}
