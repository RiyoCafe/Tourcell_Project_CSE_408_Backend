package com.example.demo_1.Payload.Request;

import com.example.demo_1.Entity.ReservationChoice;
import lombok.*;

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
    List<ReservationChoice> reservationChoices;
}
