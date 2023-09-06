package com.example.demo_1.Payload.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PackageReviewResponse {
    Long reviewUuid;
    Long customerUuid;
    Long packageUuid;
    String reviewContent;
    double packageRatingIndividual;
    double packageRatingOverall;
    int packageRatingCnt;
}
