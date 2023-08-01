package com.example.demo_1.Payload.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
@ToString
public class PackageFilterRequest {
    Timestamp startTimestamp;
    int durationMin;
    int durationMax;
    int hotelStarMin;
    //activityTags to show fun things to do
    List<String> activityTags;
    List<String> activityPlaces;

    Double packageRating;
    int priceMin;
    int priceMax;

    String sortBy;


}
