package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.PackageRequest;
import com.example.demo_1.Payload.Response.MessageResponse;
import com.example.demo_1.Payload.Response.PackageDetailsResponse;
import com.example.demo_1.Repository.LocationRepository;
import com.example.demo_1.Repository.PackageRepository;
import com.example.demo_1.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PackageController {
    @Autowired
    private PackageRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private FlightDetailsService flightDetailsService;
    @Autowired
    private HotelPackageService hotelPackageService;
    @Autowired
    private LocationRepository locationRepository;
    @GetMapping("/api/packages")
    public ResponseEntity<?> getAllpackages()
    {
        List<PackageDetailsResponse> responses=new ArrayList<>();
        List<Package> packageList = repository.findAll();
        for (Package p:packageList){
            PackageDetailsResponse tempResponse = packageService.response(p.getUuid());
            responses.add(tempResponse);
        }
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/api/vendor/packages")
    public ResponseEntity<?> getPackagesOfVendor()
    {
        List<PackageDetailsResponse> responses=new ArrayList<>();
        Long vendor_uuid= userService.getMyUserUuid();
        List<Package> packageList = repository.findAllByVendorUuid(vendor_uuid);
        for (Package p:packageList){
            PackageDetailsResponse tempResponse = packageService.response(p.getUuid());
            responses.add(tempResponse);
        }
        return ResponseEntity.ok(responses);

    }
    @GetMapping("/api/vendor/packages/{package_uuid}")
    public ResponseEntity<?> getPackageOverview(@PathVariable Long package_uuid){
        PackageDetailsResponse response = packageService.response(package_uuid);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/api/vendor/packages")
    public ResponseEntity<?> addNewPackage(@RequestBody PackageRequest packageRequest){
        Long vendorUuid = userService.getMyUserUuid();

        Package newPackage = new Package();
        newPackage.setName(packageRequest.getPackageName());
        newPackage.setLoactionUuid(packageRequest.getLocationUuid());
        newPackage.setPrice(packageRequest.getPrice());
        newPackage.setDurationDays(packageRequest.getDurationDays());
        newPackage.setOverview(packageRequest.getDescriptionPackage());
        newPackage.setStartTimestamp(packageRequest.getStartTimestamp());
        newPackage.setVendorUuid(vendorUuid);
        newPackage.setRatingCnt(0);
        newPackage.setRating(0.0);
        newPackage.setReservationCnt(0);
        newPackage.setHotelMinRating(100);

        Package savedPackage = repository.save(newPackage);

        flightDetailsService.addFlightAndFlightOptions(packageRequest, savedPackage.getUuid());
        hotelPackageService.addHotelPackageAndHotelPackageOptions(packageRequest, savedPackage.getUuid());
        activityService.saveActivities(packageRequest, savedPackage.getUuid());


        return ResponseEntity.ok(new MessageResponse("Package Added Successfully"));

    }
    @PutMapping("/api/vendor/packages/{package_uuid}")
    public ResponseEntity<?> updatePackage(@RequestBody PackageRequest packageRequest,@PathVariable Long package_uuid){
        Package updatePackage = repository.findByUuid(package_uuid);
        updatePackage.setName(packageRequest.getPackageName());
        updatePackage.setLoactionUuid(packageRequest.getLocationUuid());
        updatePackage.setPrice(packageRequest.getPrice());
        updatePackage.setDurationDays(packageRequest.getDurationDays());
        updatePackage.setOverview(packageRequest.getDescriptionPackage());
        updatePackage.setVendorUuid(updatePackage.getVendorUuid());
        repository.save(updatePackage);

        String response1 = flightDetailsService.saveFlightAndFlightOptions(packageRequest,package_uuid);
        String response2 = hotelPackageService.saveHotelPackageAndHotelPackageOptions(packageRequest,package_uuid);
        activityService.saveActivities(packageRequest,package_uuid);
        return ResponseEntity.ok(new MessageResponse(response1+" "+response2));
    }

    @DeleteMapping("/api/vendor/packages/{package_uuid}")
    public ResponseEntity<?> deletePackage(@PathVariable Long package_uuid)
    {
        return ResponseEntity.ok(new MessageResponse("Deleted Package Successfully"));
    }

}
