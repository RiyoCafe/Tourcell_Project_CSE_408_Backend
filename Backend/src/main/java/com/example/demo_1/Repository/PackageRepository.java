package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public interface PackageRepository extends JpaRepository<Package,Long> {
    Package findByUuid(Long uuid);
    void deleteByUuid(Long uuid);
    List<Package> findAllByVendorUuid(Long vendor_uuid);


    List<Package> findAllByLoactionUuidAndStartTimestampAfter(Long locationUuid, Timestamp startTimestamp);

    List<Package> findTop5ByOrderByPriceAsc();


    List<Package> findTop5ByOrderByRatingDesc();


}
