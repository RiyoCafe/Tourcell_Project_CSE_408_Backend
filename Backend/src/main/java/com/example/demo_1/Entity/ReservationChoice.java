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
    Long choiceUuid;//uuid of hotel_package_options/flight_options/activity
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    ChoiceType choiceType;
}
