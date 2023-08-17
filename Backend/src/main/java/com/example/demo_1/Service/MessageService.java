package com.example.demo_1.Service;

import com.example.demo_1.Entity.Message;
import com.example.demo_1.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getPreviousMessageHistory(Long uuid1,Long uuid2)
    {
        return messageRepository.findAllByEitherSenderOrReceiver(uuid1, uuid2);
    }
}
