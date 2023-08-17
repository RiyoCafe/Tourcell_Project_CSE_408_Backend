package com.example.demo_1.Service;

import com.example.demo_1.Entity.Notification;
import com.example.demo_1.Entity.NotificationType;
import com.example.demo_1.Repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void makeNotification(Long fromUuid,Long toUuid,NotificationType type,String name)
    {
        String str = new String();
        Notification newNotification = new Notification();
        newNotification.setDelivered(false);
        newNotification.setRead(false);
        newNotification.setFromUuid(fromUuid);
        newNotification.setToUuid(toUuid);
        newNotification.setNotificationType(type);
        if(type == NotificationType.MESSAGE) str = "New message from "+fromUuid;
        if(type == NotificationType.BOOKING) str = "Booking request for "+name+" package from "+fromUuid;
        newNotification.setContent(str);
        createNotification(newNotification);
    }

    public Notification createNotification(Notification notification)
    {
        return notificationRepository.save(notification);
    }
    public Notification getNotificationByID(Long uuid)
    {
        Notification notification = notificationRepository.findByUuid(uuid);
        if(notification == null){
            throw  new RuntimeException("notification not found!");
        }
        return  notification;
    }

    public List<Notification> getNotificationsByToUuidIDNotDelivered(Long toUuid){
        return notificationRepository.findAllByToUuidAndDeliveredFalse(toUuid);
    }
    public List<Notification> getNotificationsByToUuid(Long toUuid) {
        return notificationRepository.findAllByToUuid(toUuid);
    }

    public Notification changeNotificationStatusToRead(Long uuid) {
        Notification notification = notificationRepository.findByUuid(uuid);
        if(notification == null){
            throw  new RuntimeException("notification not found!");
        }
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public void clear() {
        notificationRepository.deleteAll();
    }

}
