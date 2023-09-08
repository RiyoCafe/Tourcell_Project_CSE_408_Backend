package com.example.demo_1.Controller;

import com.example.demo_1.Service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/public/notification")
@Slf4j
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{toUuid}")
    public ResponseEntity<?> getNotificationsByVendorUuid(@PathVariable Long toUuid){
        return ResponseEntity.ok(notificationService.getNotificationsByToUuid(toUuid));
    }

    @PatchMapping("/read/{notificationUuid}")
    public ResponseEntity changeNotifStatusToRead(@PathVariable Long notificationUuid) {
        return ResponseEntity.ok(notificationService.changeNotificationStatusToRead(notificationUuid));
    }

}
