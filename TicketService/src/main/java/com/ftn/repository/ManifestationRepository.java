package com.ftn.repository;

import com.ftn.model.Manifestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManifestationRepository extends JpaRepository<Manifestation, Long> {
}
