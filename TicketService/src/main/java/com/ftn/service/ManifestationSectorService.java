package com.ftn.service;

import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.ManifestationSector;
import com.ftn.repository.ManifestationSectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManifestationSectorService {

    @Autowired
    ManifestationSectorRepository manSecRepo;

    public ManifestationSector getSectorPriceById(Long id){

        ManifestationSector ms = manSecRepo.findById(id).orElseThrow(()->new EntityNotFoundException(
        "Sector price with id : "+id+" not found."));

        return ms;
    }

    public List<ManifestationSector> getAllSectorPrices(){
        return manSecRepo.findAll();
    }

    public ManifestationSector getSectorPriceByDayAndSector(Long idDay, Long idSector){

        for(ManifestationSector ms : getAllSectorPrices()){
            if(ms.getSector().getId() == idSector && ms.getManifestationDays().getId() == idDay){
                return ms;
            }
        }

        return null;
    }
}
