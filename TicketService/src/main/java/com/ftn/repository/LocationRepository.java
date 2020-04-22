package com.ftn.repository;

import com.ftn.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

     Location findByLocationName(String name);

     /* Not in use, but can by helpful

     @Query("select l from Location l join fetch l.sectors where l.id = ?1")
     Location findByIdAndFetchSectors(Long id);
     */
}
