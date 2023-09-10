package com.example.demo_1.Payload.Response;

import com.example.demo_1.Entity.Activity;
import com.example.demo_1.Entity.Package;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class PackageDetailsResponse {
    Long packageUuid;
    String name;
    String locationName;
    int price;
    int durationDays;
    String overview;
    Double rating;
    String vendorName;
    List<HotelPackageDetailsResponse> hotelResponses;
    //same as flight and activity
    List<FlightDetailsResponse> flightResponses;
    List<Activity> activityResponses;
    Boolean availability;
}
