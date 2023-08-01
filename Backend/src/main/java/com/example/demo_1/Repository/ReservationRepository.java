package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Reservation;
import com.example.demo_1.Entity.ReservationChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Reservation findByUuid(Long uuid);
    List<Reservation> findAllByCustomerUuid(Long customerUuid);
}
