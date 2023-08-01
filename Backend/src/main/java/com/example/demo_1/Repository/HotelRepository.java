package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Long> {

    Hotel findByUuid(Long uuid);
    void deleteByUuid(Long uuid);

    @Query("select min(star) from Hotel where uuid in ?1")
    int findMinStarByUuidsIn(List<Long> uuids);

}
