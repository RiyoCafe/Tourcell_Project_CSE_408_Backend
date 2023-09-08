package com.example.demo_1.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Data
@Entity
@Getter
@Setter
@ToString
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long uuid;
    Long customerUuid;
    Long packageUuid;
    @ColumnDefault("2")
    @JsonIgnore
    Long vendorUuid;
    int totalCost;
    @JsonIgnore
    Double reviewRating;
    Timestamp timestamp;
}
