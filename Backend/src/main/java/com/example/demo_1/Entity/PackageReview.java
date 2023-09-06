package com.example.demo_1.Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "package_review")
public class PackageReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uuid;
    Long customerUuid;
    Long packageUuid;
    String reviewContent;
    double givenPackageRating;
}
