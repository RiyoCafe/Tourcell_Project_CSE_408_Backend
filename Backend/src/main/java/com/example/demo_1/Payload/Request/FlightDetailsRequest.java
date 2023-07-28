package com.example.demo_1.Payload.Request;

import com.example.demo_1.Entity.FlightOptions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
public class FlightDetailsRequest {

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
