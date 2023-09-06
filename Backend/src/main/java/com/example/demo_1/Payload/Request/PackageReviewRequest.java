package com.example.demo_1.Payload.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PackageReviewRequest {
    Long customerUuid;
    Long packageUuid;
    String reviewContent;
    double givenPackageRating;
}
