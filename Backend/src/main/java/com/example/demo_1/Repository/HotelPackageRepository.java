package com.example.demo_1.Repository;

import com.example.demo_1.Entity.HotelPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelPackageRepository extends JpaRepository<HotelPackage,Long> {
    HotelPackage findByUuid(Long uuid);
    List<HotelPackage> findAllByPackageUuid(Long packageUuid);
}
