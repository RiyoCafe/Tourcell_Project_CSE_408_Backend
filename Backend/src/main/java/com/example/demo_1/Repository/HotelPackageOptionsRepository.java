package com.example.demo_1.Repository;

import com.example.demo_1.Entity.HotelPackageOptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelPackageOptionsRepository extends JpaRepository<HotelPackageOptions,Long> {
    HotelPackageOptions findByUuid(Long uuid);
    List<HotelPackageOptions> findAllByHotelPackageUuid(Long hotelPackageUuid);
    void deleteByUuid(Long uuid);
}
