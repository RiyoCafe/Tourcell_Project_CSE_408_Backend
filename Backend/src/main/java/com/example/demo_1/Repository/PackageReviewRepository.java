package com.example.demo_1.Repository;

import com.example.demo_1.Entity.PackageReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageReviewRepository extends JpaRepository<PackageReview,Long> {
    PackageReview findByUuid(Long uuid);
    List<PackageReview> findByPackageUuid(Long packageUuid);
    void deleteByUuid(Long uuid);
}
