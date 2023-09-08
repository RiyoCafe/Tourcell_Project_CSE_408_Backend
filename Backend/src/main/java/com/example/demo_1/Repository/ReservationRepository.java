package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Reservation;
import com.example.demo_1.Entity.ReservationChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Reservation findByUuid(Long uuid);
    List<Reservation> findAllByCustomerUuid(Long customerUuid);

    List<Reservation> findAllByTimestampAfterAndVendorUuid(Timestamp timestamp,Long vendorUuid);
    List<Reservation> findAllByTimestampBeforeAndVendorUuid(Timestamp timestamp,Long vendorUuid);
    List<Reservation> findAllByTimestampAfterAndCustomerUuid(Timestamp timestamp,Long customerUuid);
    List<Reservation> findAllByTimestampBeforeAndCustomerUuid(Timestamp timestamp,Long customerUuid);

}

