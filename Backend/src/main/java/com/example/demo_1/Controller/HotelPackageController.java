package com.example.demo_1.Controller;

import com.example.demo_1.Entity.HotelPackage;
import com.example.demo_1.Repository.HotelPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class HotelPackageController {
    @Autowired
    private HotelPackageRepository hotelPackageRepository;
    //@PutMapping("/api/vendor/hotelPackage/{hotel_package_id}")
//    public ResponseEntity<?> updateHotelPackage(@PathVariable Long hotel_package_id){
//        HotelPackage updatedHotelPackage = hotelPackageRepository.findByUuid(hotel_package_id);
//
//
//    }

}
