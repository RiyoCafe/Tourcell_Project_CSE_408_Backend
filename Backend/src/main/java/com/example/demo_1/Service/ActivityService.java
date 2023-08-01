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
        updatedActivity.setImageUrl(activity.getImageUrl());
        updatedActivity.setStartTimestamp(activity.getStartTimestamp());
        updatedActivity.setDurationMinutes(activity.getDurationMinutes());
        updatedActivity.setPackageUuid(pack.getUuid());
        updatedActivity.setLocationUuid(pack.getLoactionUuid());
        updatedActivity.setReservedCount(pack.getReservationCnt());
        updatedActivity.setPlaceName(activity.getPlaceName());
        updatedActivity=activityRepository.save(updatedActivity);
        return updatedActivity;
    }

    public void saveActivities(PackageRequest request,Long package_uuid)
    {
        List<Activity> activities = request.getActivityRequests();
        for(Activity activity:activities) {
            singleActivitySave(activity,package_uuid);
        }
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
