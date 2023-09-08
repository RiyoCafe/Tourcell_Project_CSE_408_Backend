package com.example.demo_1.Payload.Request;

import com.example.demo_1.Entity.ReservationChoice;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    Long customerUuid;
    Long packageUuid;
    int totalCost;
    Timestamp timestamp;
    List<ReservationChoice> reservationChoices;
}
