package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight,Long> {
    List<Flight> findAllByPackageUuid(Long packageUuid);
    Flight findByUuid(Long uuid);
    void deleteByUuid(Long uuid);
}
