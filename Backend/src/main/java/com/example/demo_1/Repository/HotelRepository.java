package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    Hotel findByUuid(Long uuid);
    void deleteByUuid(Long uuid);

}
