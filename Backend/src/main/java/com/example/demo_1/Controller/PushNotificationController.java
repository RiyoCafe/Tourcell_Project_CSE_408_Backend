package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Notification;
import com.example.demo_1.Service.PushNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/public/vendor/push-notifications")
@Slf4j
public class PushNotificationController {
    @Autowired
    private PushNotificationService pushNotificationService;

    @GetMapping("/{toUuid}")
    public Flux<ServerSentEvent<List<Notification>>> streamLastMessage(@PathVariable Long toUuid) {
        return pushNotificationService.getNotificationsByToUuid(toUuid);
    }
}
