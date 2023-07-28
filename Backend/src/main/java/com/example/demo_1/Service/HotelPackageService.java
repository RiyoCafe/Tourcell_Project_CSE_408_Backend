package com.example.demo_1.Service;

import com.example.demo_1.Entity.Hotel;
import com.example.demo_1.Entity.HotelPackage;
import com.example.demo_1.Entity.HotelPackageOptions;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.HotelPackageDetailsRequest;
import com.example.demo_1.Payload.Request.PackageRequest;
import com.example.demo_1.Payload.Response.HotelPackageDetailsResponse;
import com.example.demo_1.Repository.HotelPackageOptionsRepository;
import com.example.demo_1.Repository.HotelPackageRepository;
import com.example.demo_1.Repository.HotelRepository;
import com.example.demo_1.Repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.min;

@Service
public class HotelPackageService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private HotelPackageRepository hotelPackageRepository;
    @Autowired
    private HotelPackageOptionsRepository hotelPackageOptionsRepository;
    private HotelPackageDetailsResponse createHotelPackageDetails(HotelPackage hotelPackage)
    {
        Hotel hotel= hotelRepository.findByUuid(hotelPackage.getHotelUuid());

        List<HotelPackageOptions> options = hotelPackageOptionsRepository.findAllByHotelPackageUuid(hotelPackage.getUuid());
        return new HotelPackageDetailsResponse(hotelPackage.getUuid(), hotel.getName(), hotel.getStar(),hotel.getLatitude(),
                hotel.getLongitude(),hotel.getDescription(),hotel.getImageUrl(),hotelPackage.getChangePrice(),
                hotelPackage.getStartTimestamp(),hotelPackage.getDurationMinutes(),options);
    }

    public List<HotelPackageDetailsResponse> getHotelPackageDetails(Long package_uuid)
    {
        List<HotelPackageDetailsResponse> responses=new ArrayList<>();
        List<HotelPackage> hotelPackages = hotelPackageRepository.findAllByPackageUuid(package_uuid);
        for(HotelPackage hotelPackage : hotelPackages){
            responses.add(createHotelPackageDetails(hotelPackage));
        }
        return responses;
    }
    public void addHotelPackageAndHotelPackageOptions(PackageRequest request, Long packageUuid){
        List<HotelPackageDetailsRequest> hotelPackageRequest=request.getHotelPackageDetailsRequests();
        int ratingMin=100;
        for(HotelPackageDetailsRequest packageRequest:hotelPackageRequest){
            HotelPackage hotelPackage = new HotelPackage();
            hotelPackage.setPackageUuid(packageUuid);
            hotelPackage.setHotelUuid(packageRequest.getHotelUuid());
            hotelPackage.setChangePrice(packageRequest.getChangePrice());
            hotelPackage.setStartTimestamp(packageRequest.getStartTimestamp());
            hotelPackage.setDurationMinutes(packageRequest.getDurationMinutes());
            HotelPackage savedHotelPackage = hotelPackageRepository.save(hotelPackage);
            Long hotelPackageUuid = savedHotelPackage.getUuid();

            Hotel hotel = hotelRepository.findByUuid(packageRequest.getHotelUuid());
            if(ratingMin>hotel.getStar()){
                ratingMin = hotel.getStar();
            }
            //System.out.println("hotel package _uuid "+hotelPackageUuid);
            List<HotelPackageOptions> options = packageRequest.getOptions();
            for(HotelPackageOptions hotelPackageOptions:options){
                hotelPackageOptions.setHotelPackageUuid(hotelPackageUuid);
                hotelPackageOptionsRepository.save(hotelPackageOptions);
                //System.out.println(abcd.getUuid()+" "+abcd.getHotelPackageUuid());
            }
        }
        Package package_=packageRepository.findByUuid(packageUuid);
        package_.setHotelMinRating(ratingMin);
        packageRepository.save(package_);
    }

    public String saveHotelPackageAndHotelPackageOptions(PackageRequest request, Long packageUuid){
        List<HotelPackageDetailsRequest> hotelPackageRequest=request.getHotelPackageDetailsRequests();
        int ratingMin =100;
        for(HotelPackageDetailsRequest hpRequest:hotelPackageRequest){
            Long hotelPackageUuid = hpRequest.getHotelPackageUuid();
            HotelPackage hotelPackage=new HotelPackage() ;
            ;

            if(hotelPackageUuid!=null)  hotelPackage.setUuid(hotelPackageUuid);
            hotelPackage.setPackageUuid(packageUuid);
            hotelPackage.setHotelUuid(hpRequest.getHotelUuid());
            hotelPackage.setChangePrice(hpRequest.getChangePrice());
            hotelPackage.setStartTimestamp(hpRequest.getStartTimestamp());
            hotelPackage.setDurationMinutes(hpRequest.getDurationMinutes());
            HotelPackage savedHotelPackage = hotelPackageRepository.save(hotelPackage);
//            hotelPackageUuid = savedHotelPackage.getUuid();
            System.out.println("joya hotel_package_uuid: "+hotelPackage.getUuid()+" "+savedHotelPackage.getUuid());
            Hotel hotel = hotelRepository.findByUuid(hpRequest.getHotelUuid());
            if(ratingMin>hotel.getStar()){
                ratingMin = hotel.getStar();
            }

            List<HotelPackageOptions> options = hpRequest.getOptions();
            for(HotelPackageOptions hotelPackageOptions:options){
                Long hotelPackageOptionUuid = hotelPackageOptions.getUuid();
                HotelPackageOptions updatedOptions = new HotelPackageOptions();
                if(hotelPackageOptionUuid!=null)    updatedOptions.setUuid(hotelPackageOptionUuid);
                updatedOptions.setBreakfastProvided(hotelPackageOptions.getBreakfastProvided());
                updatedOptions.setAirConditioned(hotelPackageOptions.getAirConditioned());
                updatedOptions.setChangePrice(hotelPackageOptions.getChangePrice());
                updatedOptions.setHotelPackageUuid(hotelPackageUuid);
                hotelPackageOptionsRepository.save(updatedOptions);
            }

        }
        Package package_=packageRepository.findByUuid(packageUuid);
        package_.setHotelMinRating(ratingMin);
        packageRepository.save(package_);

        return "saved HotelPackage and hotelPackageOptions";
    }
}
