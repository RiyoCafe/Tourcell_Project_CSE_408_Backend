package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    Activity findByUuid(Long uuid);
    List<Activity> findAllByPackageUuid(Long packageUuid);
    void deleteByUuid(Long uuid);
    List<Activity> findAllByLocationUuid(Long locationUuid);

    @Query("select distinct placeName from Activity where locationUuid=?1")
    List<String> findPlacesByLocationUuid(Long locationUuid);
    List<Activity> findAllByPackageUuidIn(List<Long> package_uuids);

    List<Activity> findTop4ByOrderByReservedCountDesc();
}
