package com.example.demo_1.Payload.Response;

import com.example.demo_1.Entity.ReservationChoice;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    Long reservationUuid;
    Long customerUuid;
    Long packageUuid;
    String packageName;
    String locationName;
    int durationDays;
    Timestamp startTimestamp;
    String vendorName;
    int totalCost;
    //paid/unpaid
    List<String> reservationChoices;
}
