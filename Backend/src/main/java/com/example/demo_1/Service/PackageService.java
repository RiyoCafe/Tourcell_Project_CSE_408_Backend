package com.example.demo_1.Service;

import com.example.demo_1.Entity.*;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.PackageFilterRequest;
import com.example.demo_1.Payload.Response.PackageDetailsResponse;
import com.example.demo_1.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
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
    @Autowired
    private ActivityService activityService;


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
                flightDetailsService.getFlightDetails(package_uuid),activities,p.isAvailable());
    }

    public PackageDetailsResponse deletePackage(Long packageUuid)
    {
        PackageDetailsResponse response = response(packageUuid);
        hotelPackageService.deleteHotelPackageByPackageUuid(packageUuid);
        flightDetailsService.deleteFlightAndFlightOptionsByPackageUuid(packageUuid);
        activityService.deleteActivityByPackageUuid(packageUuid);
        packageRepository.deleteByUuid(packageUuid);
        return response;
    }

    public List<Package> applyPriceFilter(List<Package> packageList, int priceMin, int priceMax)
    {
        List<Package> packages = new ArrayList<>();
        for(Package pack:packageList)
        {
            if(pack.getPrice()>=priceMin && pack.getPrice()<=priceMax)
                packages.add(pack);
        }
        return packages;
    }
    public List<Package> applyDurationFilter(List<Package> packageList, int durationMin, int durationMax)
    {
        List<Package> packages = new ArrayList<>();
        for(Package pack:packageList)
        {
            if(pack.getDurationDays()>=durationMin && pack.getDurationDays()<=durationMax)
                packages.add(pack);
        }
        return packages;
    }
    public List<Package> applyHotelStarMinFilter(List<Package> packageList,int star)
    {
        List<Package> packages = new ArrayList<>();
        for(Package pack:packageList)
        {
            if(pack.getHotelMinRating()>=star)  packages.add(pack);
        }
        return packages;
    }

    public List<Package> applyPackageRatingFilter(List<Package> packageList,Double package_rating)
    {
        List<Package> packages = new ArrayList<>();
        for(Package pack:packageList)
        {
            if(pack.getRating()>=package_rating)  packages.add(pack);
        }
        return packages;
    }
    public List<Package> applyActivityTags(List<Package> packageList,List<String> activityTags)
    {
       List<Package> packages = new ArrayList<>();
       for(Package pack:packageList)
       {
           List<Activity> activities = activityRepository.findAllByPackageUuid(pack.getUuid());
           boolean foundAll = true;
           for(String tags:activityTags)
           {
               boolean found = false;
               for(Activity activity:activities)
               {
                   if(activity.getName().equalsIgnoreCase(tags)){
                       found = true;
                       break;
                   }
               }
               if(found == false)   foundAll=false;
           }
           if(foundAll)     packages.add(pack);
       }
       return packages;

    }
    public List<Package> applyActivityPlaces(List<Package> packageList,List<String> activityPlaceName){
        List<Package> packages = new ArrayList<>();
        for(Package pack:packageList){
            List<Activity> activities = activityRepository.findAllByPackageUuid(pack.getUuid());
            boolean foundAll = true;
            for(String placeName:activityPlaceName){
                boolean found = false;
                for(Activity activity:activities){
                    if(activity.getPlaceName().equalsIgnoreCase(placeName)){
                        found=true;
                        break;
                    }
                }
                if(found == false)  foundAll=false;
            }
            if(foundAll)    packages.add(pack);
        }
        return packages;
    }

    private int compareTo(Double value)
    {
        if(value<0) return -1;
        if(value>0) return 1;
        return 0;
    }
    public List<Package> applySortByPrice(List<Package>packages)
    {
        Collections.sort(packages, new Comparator<Package>() {
            @Override
            public int compare(Package t1, Package t2) {
                return compareTo((t1.getPrice()-t2.getPrice())*1.0);
            }
        });
        return packages;
    }
    public List<Package> applySortByRating(List<Package> packages)
    {
        Collections.sort(packages, new Comparator<Package>() {
            @Override
            public int compare(Package t1, Package t2) {
                return compareTo(t2.getRating()-t1.getRating());
            }
        });
        return packages;
    }
    public List<PackageDetailsResponse> packagesWithFilter(PackageFilterRequest request,Long locationUuid)
    {
        System.out.println("request "+request);
        Location searchedLocation = locationRepository.findByUuid(locationUuid);
        int cnt = searchedLocation.getSearchCnt()+1;
        searchedLocation.setSearchCnt(cnt);
        locationRepository.save(searchedLocation);
        //List<Package> packageList = packageRepository.findAllByLoactionUuidAndStartTimestampAfter(locationUuid,request.getStartTimestamp());
        List<Package> packageList = packageRepository.findAllByLoactionUuidAndAvailableTrue(locationUuid);

        if(request.getDurationMax()!=0){
            packageList = applyDurationFilter(packageList,request.getDurationMin(),request.getDurationMax());
        }

        if(request.getPriceMax()!=0){
            packageList = applyPriceFilter(packageList,request.getPriceMin(),request.getPriceMax());
        }

        if(request.getHotelStarMin()!=0){
            packageList = applyHotelStarMinFilter(packageList,request.getHotelStarMin());
        }

        if(request.getPackageRating()!=null){
            packageList = applyPackageRatingFilter(packageList,request.getPackageRating());
        }

        if(request.getActivityTags() != null)
        {
            packageList = applyActivityTags(packageList,request.getActivityTags());
        }

        if(request.getActivityPlaces() != null){
            packageList = applyActivityPlaces(packageList,request.getActivityPlaces());
        }
        if(request.getSortBy()!=null){
            if(request.getSortBy() .equalsIgnoreCase("Price") )
            {
                packageList = applySortByPrice(packageList);
            }
            if(request.getSortBy().equalsIgnoreCase("Rating"))
            {
                packageList = applySortByRating(packageList);
            }
        }

        List<PackageDetailsResponse> responses = new ArrayList<>();
        for(Package p:packageList)
        {
            responses.add(getPackageDetailsResponse(p));
        }
        return responses;
    }
    private PackageDetailsResponse getPackageDetailsResponse( Package p)
    {
        User vendor= userRepository.findByUuid(p.getVendorUuid());
        Location location=locationRepository.findByUuid(p.getLoactionUuid());
        List<Activity> activities = activityRepository.findAllByPackageUuid(p.getUuid());
        return new PackageDetailsResponse(p.getUuid(),p.getName(), location.getCity()+" "+location.getCountry(),
                p.getPrice(),p.getDurationDays(),p.getOverview(),p.getRating(),
                vendor.getFirstname()+" "+vendor.getLastname(),
                hotelPackageService.getHotelPackageDetails(p.getUuid()),
                flightDetailsService.getFlightDetails(p.getUuid()),activities,p.isAvailable());
    }

    public List<PackageDetailsResponse> getPackagesInOrder(String sortBy) {
        List<Package> packageList =new ArrayList<>();
        //System.out.println("sortBy is "+sortBy);
        if(sortBy.equalsIgnoreCase("Price") ){
            System.out.println("hello");
            packageList = packageRepository.findTop4ByOrderByPriceAsc();
        }
        if(sortBy.equalsIgnoreCase("Rating"))
        {
            packageList = packageRepository.findTop4ByOrderByRatingDesc();
        }
        //System.out.println(packageList);
        List<PackageDetailsResponse> responses=new ArrayList<>();
        for(Package p: packageList)
        {
            if(!p.isAvailable())    continue;
            responses.add(getPackageDetailsResponse(p));
        }
        return responses;
    }

    public Package toggleAvailableStatus(Long packageUuid) {
        Package pack = packageRepository.findByUuid(packageUuid);
        boolean availableFlag = pack.isAvailable();
        if(availableFlag)   availableFlag = false;
        else                availableFlag = true;
        pack.setAvailable(availableFlag);
        pack = packageRepository.save(pack);
        return pack;
    }

    public boolean getAvailaleStatus(Long packageUuid) {
        Package pack = packageRepository.findByUuid(packageUuid);
        return pack.isAvailable();
    }
}
