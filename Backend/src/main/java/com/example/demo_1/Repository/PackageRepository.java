package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package,Long> {
    Package findByUuid(Long uuid);
    Package findByUuidAndVendorUuid(Long uuid,Long vendor_uuid);
    List<Package> findAllByVendorUuid(Long vendor_uuid);

}
