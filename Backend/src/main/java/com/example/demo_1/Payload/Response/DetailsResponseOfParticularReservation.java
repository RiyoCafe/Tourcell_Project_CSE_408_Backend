package com.example.demo_1.Payload.Response;

import com.example.demo_1.Entity.Activity;
import com.example.demo_1.Entity.FlightOptions;
import com.example.demo_1.Entity.HotelPackageOptions;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DetailsResponseOfParticularReservation {
    String packageName;
    String customerName;
    String customerEmail;
    String vendorName;
    String vendorEmail;
    int totalCost;
    Timestamp startTimeOfTour;
    String hotelName;
    HotelPackageOptions hotelPackageOptions;
    String flightName;
    FlightOptions flightOptions;
    List<Activity> activityList;

}
