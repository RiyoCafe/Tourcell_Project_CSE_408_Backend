package com.example.demo_1.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@Getter
@Setter
@ToString
@Table(name = "hotel_package_options")
public class HotelPackageOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uuid;

    Long hotelPackageUuid;
    Boolean breakfastProvided;
    Boolean airConditioned;
    int changePrice;

}
