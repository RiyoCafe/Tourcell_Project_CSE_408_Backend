package com.example.demo_1.Service;

import com.example.demo_1.Entity.Activity;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.PackageRequest;
import com.example.demo_1.Payload.Response.MessageResponse;
import com.example.demo_1.Repository.ActivityRepository;
import com.example.demo_1.Repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private PackageRepository packageRepository;


    public Activity singleActivitySave(Activity activity,Long package_uuid)
    {
        Package pack = packageRepository.findByUuid(package_uuid);
        Long activityUuid = activity.getUuid();
        Activity updatedActivity = new Activity();
        if(activityUuid!=null)  updatedActivity.setUuid(activityUuid);
        updatedActivity.setName(activity.getName());
        updatedActivity.setDescription(activity.getDescription());
        if(activity.getActivityPrice()<0) {
            return null;
        }
        updatedActivity.setActivityPrice(activity.getActivityPrice());//newly added
        updatedActivity.setImageUrl(activity.getImageUrl());
        LocalDateTime localDateTime = activity.getStartTimestamp().toLocalDateTime();
        if(localDateTime.isBefore(LocalDateTime.now())){
            return null;
        }
        updatedActivity.setStartTimestamp(activity.getStartTimestamp());
        updatedActivity.setDurationMinutes(activity.getDurationMinutes());
        updatedActivity.setPackageUuid(pack.getUuid());
        updatedActivity.setLocationUuid(pack.getLoactionUuid());
        updatedActivity.setReservedCount(pack.getReservationCnt());
        updatedActivity.setPlaceName(activity.getPlaceName());
        updatedActivity=activityRepository.save(updatedActivity);
        return updatedActivity;
    }

    public boolean saveActivities(PackageRequest request,Long package_uuid)
    {
        List<Activity> activities = request.getActivityRequests();
        Activity returnActivity ;
        for(Activity activity:activities) {
            returnActivity=singleActivitySave(activity,package_uuid);
            if(returnActivity == null){
                return false;
            }
        }
        return true;
    }

    public Activity deleteActivity(Long activityUuid)
    {
        Activity deletedActivity = activityRepository.findByUuid(activityUuid);
        activityRepository.deleteByUuid(activityUuid);
        return deletedActivity;
    }

    public ResponseEntity<?> deleteActivityByPackageUuid(Long packageUuid) {
        List<Activity> activities = activityRepository.findAllByPackageUuid(packageUuid);
        for(Activity activity:activities){
            deleteActivity(activity.getUuid());
        }
        return ResponseEntity.ok(new MessageResponse("Deleted activity successfully from deleteActivityByPackageUuid"));
    }
}
