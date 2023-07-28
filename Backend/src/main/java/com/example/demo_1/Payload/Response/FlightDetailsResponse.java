package com.example.demo_1.Payload.Response;

import com.example.demo_1.Entity.FlightOptions;
import lombok.*;
import java.sql.Timestamp;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FlightDetailsResponse {
    Long flightUuid;
    String airlinesNames;
    Long startLocationUuid;
    Long endLocationUuid;
    Timestamp startTimestamp;
    String imageUrl;
    int durationMinutes;
    int changePrice;
    String description;
    List<FlightOptions> optionsList;
}
