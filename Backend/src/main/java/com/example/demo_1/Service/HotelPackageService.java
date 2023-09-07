package com.example.demo_1.Service;

import com.example.demo_1.Entity.Hotel;
import com.example.demo_1.Entity.HotelPackage;
import com.example.demo_1.Entity.HotelPackageOptions;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.HotelPackageDetailsRequest;
import com.example.demo_1.Payload.Request.PackageRequest;
import com.example.demo_1.Payload.Response.HotelPackageDetailsResponse;
import com.example.demo_1.Payload.Response.MessageResponse;
import com.example.demo_1.Repository.HotelPackageOptionsRepository;
import com.example.demo_1.Repository.HotelPackageRepository;
import com.example.demo_1.Repository.HotelRepository;
import com.example.demo_1.Repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
@Transactional
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

    public List<HotelPackageDetailsResponse> getHotelPackageDetails(Long packageUuid)
    {
        List<HotelPackageDetailsResponse> responses=new ArrayList<>();
        List<HotelPackage> hotelPackages = hotelPackageRepository.findAllByPackageUuid(packageUuid);
        for(HotelPackage hotelPackage : hotelPackages){
            responses.add(createHotelPackageDetails(hotelPackage));
        }
        return responses;
    }
    private HotelPackage saveHotelPackage(HotelPackageDetailsRequest hpRequest,Long packageUuid)
    {
        Long hotelPackageUuid = hpRequest.getHotelPackageUuid();
        HotelPackage hotelPackage=new HotelPackage() ;
        if(hotelPackageUuid!=null)  hotelPackage.setUuid(hotelPackageUuid);
        hotelPackage.setPackageUuid(packageUuid);
        hotelPackage.setHotelUuid(hpRequest.getHotelUuid());
        hotelPackage.setChangePrice(hpRequest.getChangePrice());
        hotelPackage.setStartTimestamp(hpRequest.getStartTimestamp());
        hotelPackage.setDurationMinutes(hpRequest.getDurationMinutes());
        HotelPackage savedHotelPackage = hotelPackageRepository.save(hotelPackage);
        return savedHotelPackage;
    }

    private HotelPackageOptions saveHotelPackageOptions(HotelPackageOptions hotelPackageOptions,Long hotelPackageUuid)
    {
        Long hotelPackageOptionUuid = hotelPackageOptions.getUuid();
        HotelPackageOptions updatedOptions = new HotelPackageOptions();
        if(hotelPackageOptionUuid!=null)    updatedOptions.setUuid(hotelPackageOptionUuid);

        updatedOptions.setBreakfastProvided(hotelPackageOptions.getBreakfastProvided());
        updatedOptions.setAirConditioned(hotelPackageOptions.getAirConditioned());
        updatedOptions.setBarProvided(hotelPackageOptions.getBarProvided());
        updatedOptions.setLunchProvided(hotelPackageOptions.getLunchProvided());
        updatedOptions.setSwimmingPoolProvided(hotelPackageOptions.getSwimmingPoolProvided());
        updatedOptions.setFreeWifiProvided(hotelPackageOptions.getFreeWifiProvided());
        updatedOptions.setParkingProvided(hotelPackageOptions.getParkingProvided());
        updatedOptions.setMassageProvided(hotelPackageOptions.getMassageProvided());
        updatedOptions.setRoomCleanProvided(hotelPackageOptions.getRoomCleanProvided());
        updatedOptions.setFitnessCenterProvided(hotelPackageOptions.getFitnessCenterProvided());
        updatedOptions.setLaundryProvided(hotelPackageOptions.getLaundryProvided());

        updatedOptions.setChangePrice(hotelPackageOptions.getChangePrice());
        updatedOptions.setHotelPackageUuid(hotelPackageUuid);
        HotelPackageOptions updatedHotelPackageOption=hotelPackageOptionsRepository.save(updatedOptions);
        return updatedHotelPackageOption;
    }

//    public void addHotelPackageAndHotelPackageOptions(PackageRequest request, Long packageUuid){
//        List<HotelPackageDetailsRequest> hotelPackageRequest=request.getHotelPackageDetailsRequests();
//        int ratingMin=100;
//        for(HotelPackageDetailsRequest packageRequest:hotelPackageRequest){
//            HotelPackage savedHotelPackage = saveHotelPackage(packageRequest,packageUuid);
//            Long hotelPackageUuid = savedHotelPackage.getUuid();
//
//            Hotel hotel = hotelRepository.findByUuid(packageRequest.getHotelUuid());
//            if(ratingMin>hotel.getStar()){
//                ratingMin = hotel.getStar();
//            }
//            //System.out.println("hotel package _uuid "+hotelPackageUuid);
//            List<HotelPackageOptions> options = packageRequest.getOptions();
//            for(HotelPackageOptions hotelPackageOptions:options){
//                saveHotelPackageOptions(hotelPackageOptions,hotelPackageUuid);
//                //System.out.println(abcd.getUuid()+" "+abcd.getHotelPackageUuid());
//            }
//        }
//        Package package_=packageRepository.findByUuid(packageUuid);
//        package_.setHotelMinRating(ratingMin);
//        packageRepository.save(package_);
//    }
    public HotelPackageDetailsResponse addOrUpdateSingleHotelPackage(HotelPackageDetailsRequest hpRequest,Long packageUuid)
    {
        HotelPackage savedHotelPackage = saveHotelPackage(hpRequest, packageUuid);

        List<HotelPackageOptions> options = hpRequest.getOptions();
        for(HotelPackageOptions hotelPackageOptions:options){
            saveHotelPackageOptions(hotelPackageOptions, savedHotelPackage.getUuid());
        }
        updateMinStar(packageUuid);
        return createHotelPackageDetails(savedHotelPackage);
    }
    public void updateMinStar(Long packageUuid)
    {
        List<Long> allHotelUuids= hotelPackageRepository.findAllHotelUuidsByPackageUuid(packageUuid);
        int minHotelStar = hotelRepository.findMinStarByUuidsIn(allHotelUuids);
        Package package_= packageRepository.findByUuid(packageUuid);
        package_.setHotelMinRating(minHotelStar);
    }

    public String saveHotelPackageAndHotelPackageOptions(PackageRequest request, Long packageUuid){
        List<HotelPackageDetailsRequest> hotelPackageRequest=request.getHotelPackageDetailsRequests();
        for(HotelPackageDetailsRequest hpRequest:hotelPackageRequest){
            addOrUpdateSingleHotelPackage(hpRequest,packageUuid);
        }

        return "saved HotelPackage and hotelPackageOptions";
    }

    public HotelPackageDetailsResponse saveOrUpdateHotelPackageAndOptionsByPackageUuid(HotelPackageDetailsRequest hpRequest,Long packageUuid)
    {
        HotelPackageDetailsResponse response=addOrUpdateSingleHotelPackage(hpRequest, packageUuid);
        return response;
    }


    public HotelPackageDetailsResponse deleteHotelPackage(Long hotelPackageUuid,Long packageUuid)
    {
        HotelPackage hotelPackage = hotelPackageRepository.findByUuid(hotelPackageUuid);
        HotelPackageDetailsResponse response = createHotelPackageDetails(hotelPackage);
        List<HotelPackageOptions> options = hotelPackageOptionsRepository.findAllByHotelPackageUuid(hotelPackageUuid);
        for(HotelPackageOptions option:options)
        {
            hotelPackageOptionsRepository.deleteByUuid(option.getUuid());
        }
        hotelPackageRepository.deleteByUuid(hotelPackageUuid);
        updateMinStar(packageUuid);


        return response;
    }

    public ResponseEntity<?> deleteHotelPackageByPackageUuid(Long packageUuid)
    {
        //all hotel packages delete
        List<HotelPackage> allHotelPackages= hotelPackageRepository.findAllByPackageUuid(packageUuid);
        for(HotelPackage hotelPackage:allHotelPackages)
        {
            deleteHotelPackage(hotelPackage.getUuid(),packageUuid);
        }
        return ResponseEntity.ok(new MessageResponse("deleted successfully from deleteHotelPackageByPackageUuid"));
    }
}
