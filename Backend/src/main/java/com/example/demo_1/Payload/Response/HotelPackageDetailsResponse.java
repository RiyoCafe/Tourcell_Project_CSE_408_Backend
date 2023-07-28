package com.example.demo_1.Payload.Response;

import com.example.demo_1.Entity.HotelPackageOptions;
import lombok.*;
import java.sql.Timestamp;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HotelPackageDetailsResponse {
    Long hotelPackageUuid;
    String Hotelname;
    int star;
    Double latitude;
    Double longitude;
    String description;
    String imageUrl;
    int changePrice;
    Timestamp startTimestamp;
    int durationMinutes;

    List<HotelPackageOptions> options;
}
