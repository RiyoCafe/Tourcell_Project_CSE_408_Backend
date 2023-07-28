package com.example.demo_1.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@Entity
@Getter
@Setter
@ToString
@Table(name = "hotel_package")
public class HotelPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uuid;
    Long hotelUuid;
    Long packageUuid;
    int changePrice;
    Timestamp startTimestamp;
    int durationMinutes;

}
