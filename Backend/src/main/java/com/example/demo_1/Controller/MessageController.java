package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Message;
import com.example.demo_1.Entity.Notification;
import com.example.demo_1.Entity.NotificationType;
import com.example.demo_1.Repository.MessageRepository;
import com.example.demo_1.Service.MessageService;
import com.example.demo_1.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;//dynamically send message to a user.
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private NotificationService notificationService;

    //receive message in this method
    @MessageMapping("/message") //  /app/message(whenever user need to send message through web socket)
    @SendTo("/api/public/chatroom/public")  //receiver will listen here
    public Message processPublicMessage(@Payload Message message)
    {
        message.setReceiverUuid(0L);
        message.setTimestamp(Timestamp.from(Instant.now()));
        messageRepository.save(message);
        notificationService.makeNotification(message.getSenderUuid(), message.getReceiverUuid(),NotificationType.MESSAGE,null);


        return message;
    }

    @MessageMapping("/private-message")
    public Message processPrivateMessage(@Payload Message message)
    {
        message.setTimestamp(Timestamp.from(Instant.now()));
        messageRepository.save(message);

        notificationService.makeNotification(message.getSenderUuid(), message.getReceiverUuid(),NotificationType.MESSAGE,null);

        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getReceiverUuid()),"/private",message);//user/userID/private
        return message;
    }
    @GetMapping("/api/public/messages")
    public List<Message> getAllMessages(@RequestParam Long uuid1,@RequestParam Long uuid2)
    {
        List<Message> messages = messageService.getPreviousMessageHistory(uuid1,uuid2);
        return messages;
    }
}
