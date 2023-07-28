package com.example.demo_1.Payload.Request;

import java.sql.Timestamp;
import java.util.List;

public class PackageFilterRequest {
    Timestamp startTimestamp;
    int durationMin;
    int durationMax;
    int hotelStarMin;
    //activityUuids to show fun things to do
    List<Long> activityUuids;
    List<String> activityPlaces;

    Double packageRating;
    int priceMin;
    int priceMax;

    String sortBy;


}
