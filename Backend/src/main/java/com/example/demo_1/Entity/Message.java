package com.example.demo_1.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Uuid;
    private Long senderUuid;
    private Long receiverUuid;
    private String message;
    private Timestamp timestamp;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;
}
