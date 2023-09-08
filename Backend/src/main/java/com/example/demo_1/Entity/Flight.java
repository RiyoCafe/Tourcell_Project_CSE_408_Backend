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
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long uuid;
    String airlinesNames;
    Long startLocationUuid;
    Long endLocationUuid;
    Timestamp startTimestamp;
    String imageUrl;
    int durationMinutes;
    Long packageUuid;
    int changePrice;
    String description;


}
