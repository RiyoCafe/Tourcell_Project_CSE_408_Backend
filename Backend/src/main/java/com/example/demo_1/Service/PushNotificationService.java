package com.example.demo_1.Service;

import com.example.demo_1.Entity.Notification;
import com.example.demo_1.Repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class PushNotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    private List<Notification> getNotifications(Long toUuid) {
        List<Notification> notifications = notificationRepository.findAllByToUuidAndDeliveredFalse(toUuid);
        notifications.forEach(x -> x.setDelivered(true));
        notificationRepository.saveAll(notifications);
        return notifications;
    }

    public Flux<ServerSentEvent<List<Notification>>> getNotificationsByToUuid(Long toUuid) {

        if (toUuid != null ) {
            return Flux.interval(Duration.ofSeconds(1))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<Notification>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifications(toUuid))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<List<Notification>>builder()
                .id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
    }
}
