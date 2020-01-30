package com.ftn.repository;

import com.ftn.model.ManifestationDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManifestationDaysRepository extends JpaRepository<ManifestationDays, Long> {
}
