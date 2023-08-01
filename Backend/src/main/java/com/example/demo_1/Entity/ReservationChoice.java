package com.example.demo_1.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@Getter
@Setter
@ToString
@Table(name = "reservation_choice")
public class ReservationChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uuid;
    Long reservationUuid;
    Long choiceUuid;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    ChoiceType choiceType;
}
