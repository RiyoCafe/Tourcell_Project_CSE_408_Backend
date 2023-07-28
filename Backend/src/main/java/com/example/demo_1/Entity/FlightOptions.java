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
@Table(name = "flight_options")
public class FlightOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uuid;
    @JsonIgnore
    Long flightUuid;
    Boolean foodProvided;
    Boolean businessClass;
    int changePrice;//default flight will have change_price = 0
}
