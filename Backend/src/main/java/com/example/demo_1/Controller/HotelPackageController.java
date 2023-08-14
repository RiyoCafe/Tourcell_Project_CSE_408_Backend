package com.example.demo_1.Controller;

import com.example.demo_1.Payload.Request.HotelPackageDetailsRequest;
import com.example.demo_1.Payload.Response.HotelPackageDetailsResponse;
import com.example.demo_1.Service.HotelPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class HotelPackageController {
    @Autowired
    private HotelPackageService hotelPackageService;

    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @PutMapping("/api/vendor/hotelPackage/{package_id}")
    public ResponseEntity<?> updateHotelPackage(@RequestBody HotelPackageDetailsRequest request, @PathVariable Long package_id){
        HotelPackageDetailsResponse response= hotelPackageService.
                saveOrUpdateHotelPackageAndOptionsByPackageUuid(request, package_id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @DeleteMapping("/api/vendor/package/{package_id}/hotelPackage/{hotel_package_id}")
    public ResponseEntity<?> deleteHotelPackage(@PathVariable Long hotel_package_id,@PathVariable Long package_id)
    {
        return new ResponseEntity<>(hotelPackageService.deleteHotelPackage(hotel_package_id,package_id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @PostMapping("/api/vendor/hotelPackage/{package_id}")
    public ResponseEntity<?> addHotelPackage(@RequestBody HotelPackageDetailsRequest request,@PathVariable Long package_id)
    {
        HotelPackageDetailsResponse response= hotelPackageService.
                saveOrUpdateHotelPackageAndOptionsByPackageUuid(request, package_id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
