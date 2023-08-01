package com.example.demo_1.Repository;

import com.example.demo_1.Entity.HotelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelPackageRepository extends JpaRepository<HotelPackage,Long> {
    HotelPackage findByUuid(Long uuid);
    List<HotelPackage> findAllByPackageUuid(Long packageUuid);

    @Query("select hotelUuid from HotelPackage where packageUuid=?1")
   List<Long> findAllHotelUuidsByPackageUuid(Long packageUuid);
    void deleteByUuid(Long uuid);
}
