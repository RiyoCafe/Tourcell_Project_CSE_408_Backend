package com.example.demo_1.Repository;

import com.example.demo_1.Entity.ReservationChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationChoiceRepository extends JpaRepository<ReservationChoice,Long> {
    ReservationChoice findByUuid(Long uuid);
    List<ReservationChoice> findAllByReservationUuid(Long reservationUuid);
}
