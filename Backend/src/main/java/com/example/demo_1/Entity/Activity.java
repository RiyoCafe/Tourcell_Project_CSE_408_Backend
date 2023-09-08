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
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long uuid;
    String name;
    String description;
    String imageUrl;

    Long packageUuid;
    Timestamp startTimestamp;
    int durationMinutes;
    //popular activities
    @JsonIgnore
    int reservedCount;
    @JsonIgnore
    Long locationUuid;
    String placeName;
    @ColumnDefault("0")
    int activityPrice;
}