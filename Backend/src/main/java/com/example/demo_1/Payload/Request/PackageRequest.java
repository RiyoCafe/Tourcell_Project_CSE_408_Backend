package com.example.demo_1.Payload.Request;

import com.example.demo_1.Entity.Activity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
public class PackageRequest {
    String packageName;
    Long locationUuid;
    int price;
    int durationDays;
    String descriptionPackage;
    Timestamp startTimestamp;

    List<HotelPackageDetailsRequest> hotelPackageDetailsRequests;
    List<FlightDetailsRequest> flightDetailsRequests;
    List<Activity> activityRequests;


}
