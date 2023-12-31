package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location,Long> {
    Location findByUuid(Long uuid);
    void deleteByUuid(Long uuid);
    Location findByCity(String city);
    List<Location> findTop4ByOrderBySearchCntDesc();

    List<Location>findTop4ByOrderByReservationCntDesc();
}
