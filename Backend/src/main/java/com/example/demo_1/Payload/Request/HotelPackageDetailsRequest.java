package com.example.demo_1.Payload.Request;

import com.example.demo_1.Entity.HotelPackageOptions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
public class HotelPackageDetailsRequest {

    Long hotelPackageUuid;
    Long hotelUuid;
    int changePrice;
    Timestamp startTimestamp;
    int durationMinutes;

    List<HotelPackageOptions> options;
}
