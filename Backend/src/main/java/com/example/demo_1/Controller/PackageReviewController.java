package com.example.demo_1.Controller;

import com.example.demo_1.Payload.Request.PackageReviewRequest;
import com.example.demo_1.Service.PackageReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class PackageReviewController {
    @Autowired
    PackageReviewService packageReviewService;

    @PostMapping("/api/public/review")
    public ResponseEntity<?> addNewReview(@RequestBody PackageReviewRequest packageReviewRequest){
        return packageReviewService.createOrUpdateReview(packageReviewRequest,null);
    }
    @PutMapping("/api/public/review/{reviewUuid}")
    public ResponseEntity<?> updateExistingReview(@RequestBody PackageReviewRequest packageReviewRequest, @PathVariable Long reviewUuid){
        return packageReviewService.createOrUpdateReview(packageReviewRequest,reviewUuid);
    }
    @DeleteMapping("/api/public/review/{reviewUuid}")
    public ResponseEntity<?> deleteExistingReview(@PathVariable Long reviewUuid){
        return packageReviewService.deleteReview(reviewUuid);
    }
    @GetMapping("/api/public/reviews/{packageUuid}")
    public ResponseEntity<?> getPackageReviews(@PathVariable Long packageUuid){
        return packageReviewService.getAllPackageReviewsByPackageUuid(packageUuid);
    }

}
