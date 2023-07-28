package com.example.demo_1.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Data
@Entity
@Getter
@Setter
@ToString
@Table(name = "location_package")
public class LocationPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;
    private Long locationUuid;
    private Long packageUuid;

}
