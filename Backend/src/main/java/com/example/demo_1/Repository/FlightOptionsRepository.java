package com.example.demo_1.Repository;

import com.example.demo_1.Entity.FlightOptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightOptionsRepository extends JpaRepository<FlightOptions,Long> {
   FlightOptions findByUuid(Long Uuid);
   List<FlightOptions> findAllByFlightUuid(Long flight_uuid);
}
