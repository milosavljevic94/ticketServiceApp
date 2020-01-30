package com.ftn.repository;

import com.ftn.model.ManifestationSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManifestationSectorRepository extends JpaRepository<ManifestationSector,Long> {
}
