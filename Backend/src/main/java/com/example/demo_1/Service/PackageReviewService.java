package com.example.demo_1.Service;

import com.example.demo_1.Entity.Package;
import com.example.demo_1.Entity.PackageReview;
import com.example.demo_1.Payload.Request.PackageReviewRequest;
import com.example.demo_1.Payload.Response.PackageReviewResponse;
import com.example.demo_1.Repository.PackageRepository;
import com.example.demo_1.Repository.PackageReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PackageReviewService {
    @Autowired
    PackageReviewRepository packageReviewRepository;
    @Autowired
    PackageRepository packageRepository;


    public ResponseEntity<?> createOrUpdateReview(PackageReviewRequest packageReviewRequest,Long uuid) {
        PackageReview packageReview = new PackageReview();
        if(uuid!=null)  packageReview = packageReviewRepository.findByUuid(uuid);
        packageReview.setPackageUuid(packageReviewRequest.getPackageUuid());
        packageReview.setCustomerUuid(packageReviewRequest.getCustomerUuid());
        packageReview.setReviewContent(packageReviewRequest.getReviewContent());
        //updating package info
        Package pack = packageRepository.findByUuid(packageReviewRequest.getPackageUuid());
        if(uuid==null){
            int cnt = pack.getRatingCnt()+1;
            double rate = pack.getRating()+packageReviewRequest.getGivenPackageRating();
            pack.setRatingCnt(cnt);
            pack.setRating(rate);
            packageRepository.save(pack);
        }
        else{
            //System.out.println("packkkkk "+packageReviewRequest.getGivenPackageRating());
            double rate = pack.getRating()- packageReview.getGivenPackageRating() +packageReviewRequest.getGivenPackageRating();
            //rate += packageReviewRequest.getGivenPackageRating();
            //System.out.println("rate is "+rate);
            pack.setRating(rate);
        }

        packageReview.setGivenPackageRating(packageReviewRequest.getGivenPackageRating());

        PackageReview savedReview = packageReviewRepository.save(packageReview);
        PackageReviewResponse response = new PackageReviewResponse(savedReview.getUuid(),savedReview.getCustomerUuid(),
                savedReview.getPackageUuid(), savedReview.getReviewContent(),
                savedReview.getGivenPackageRating(),pack.getRating(), pack.getRatingCnt()); //overall package rating is need to shown in frontend.
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> deleteReview(Long reviewUuid) {
        PackageReview reviewPackage = packageReviewRepository.findByUuid(reviewUuid);
        packageReviewRepository.deleteByUuid(reviewUuid);
        //new modified package rating should be saved after delete.
        Package pack = packageRepository.findByUuid(reviewPackage.getPackageUuid());
        pack.setRatingCnt(pack.getRatingCnt()-1);
        pack.setRating(pack.getRating()-reviewPackage.getGivenPackageRating());
        packageRepository.save(pack);
        PackageReviewResponse response = new PackageReviewResponse(reviewPackage.getUuid(),
                reviewPackage.getCustomerUuid(), reviewPackage.getPackageUuid(),
                reviewPackage.getReviewContent(),reviewPackage.getGivenPackageRating(),
                pack.getRating(), pack.getRatingCnt());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getAllPackageReviewsByPackageUuid(Long packageUuid) {
        List<PackageReview> reviews = packageReviewRepository.findByPackageUuid(packageUuid);
        return ResponseEntity.ok(reviews);
    }
}
