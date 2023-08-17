package com.example.demo_1.Repository;

import com.example.demo_1.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query("select m from Message m where (m.senderUuid=?1 and m.receiverUuid=?2) or (m.senderUuid=?2 and m.receiverUuid=?1)")

    List<Message> findAllByEitherSenderOrReceiver(Long uuid1,Long uuid2);

}
