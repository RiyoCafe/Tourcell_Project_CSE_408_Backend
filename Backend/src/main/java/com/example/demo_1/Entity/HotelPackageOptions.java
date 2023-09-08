package com.example.demo_1.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Getter
@Setter
@ToString
@Table(name = "hotel_package_options")
public class HotelPackageOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long uuid;

    Long hotelPackageUuid;
    @ColumnDefault("false")
    Boolean breakfastProvided;
    @ColumnDefault("false")
    Boolean airConditioned;
    @ColumnDefault("false")
    Boolean lunchProvided;
    @ColumnDefault("false")
    Boolean swimmingPoolProvided;
    @ColumnDefault("false")
    Boolean freeWifiProvided;
    @ColumnDefault("false")
    Boolean parkingProvided;
    @ColumnDefault("false")
    Boolean massageProvided;
    @ColumnDefault("false")
    Boolean roomCleanProvided;
    @ColumnDefault("false")
    Boolean fitnessCenterProvided;
    @ColumnDefault("false")
    Boolean barProvided;
    @ColumnDefault("false")
    Boolean laundryProvided;
    int changePrice;

}
