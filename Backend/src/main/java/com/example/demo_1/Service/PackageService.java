package com.example.demo_1.Service;

import com.example.demo_1.Entity.Activity;
import com.example.demo_1.Entity.Location;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Entity.User;
import com.example.demo_1.Payload.Response.PackageDetailsResponse;
import com.example.demo_1.Repository.ActivityRepository;
import com.example.demo_1.Repository.LocationRepository;
import com.example.demo_1.Repository.PackageRepository;
import com.example.demo_1.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private HotelPackageService hotelPackageService;
    @Autowired
    private FlightDetailsService flightDetailsService;


    public PackageDetailsResponse response(Long package_uuid)
    {
        Package p = packageRepository.findByUuid(package_uuid);
        User vendor= userRepository.findByUuid(p.getVendorUuid());
        Location location=locationRepository.findByUuid(p.getLoactionUuid());
        List<Activity> activities = activityRepository.findAllByPackageUuid(package_uuid);
        return new PackageDetailsResponse(package_uuid,p.getName(), location.getCity()+" "+location.getCountry(),
                p.getPrice(),p.getDurationDays(),p.getOverview(),p.getRating(),
                vendor.getFirstname()+" "+vendor.getLastname(),
                hotelPackageService.getHotelPackageDetails(package_uuid),
                flightDetailsService.getFlightDetails(package_uuid),activities);
    }

}
