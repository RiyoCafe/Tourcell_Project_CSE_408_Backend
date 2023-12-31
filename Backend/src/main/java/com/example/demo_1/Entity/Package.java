package com.example.demo_1.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@Entity
@Getter
@Setter
@ToString
@Table(name = "package")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long uuid;
    String name;
    Long loactionUuid;
    int price;
    int durationDays;
    Timestamp startTimestamp;
    String overview;
    int ratingCnt;
    Double rating;
    int hotelMinRating;
    Long vendorUuid;
    //popular packages
    int reservationCnt;
    @ColumnDefault("true")
    boolean available;
}
