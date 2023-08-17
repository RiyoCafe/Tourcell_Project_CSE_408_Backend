package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Notification;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Notification findByUuid(Long uuid);
    List<Notification> findAllByToUuid(Long toUuid);

    List<Notification> findAllByToUuidAndDeliveredFalse(Long uuid);
}
