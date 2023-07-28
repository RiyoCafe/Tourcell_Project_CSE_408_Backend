package com.example.demo_1.Service;

import com.example.demo_1.Entity.Activity;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.PackageRequest;
import com.example.demo_1.Repository.ActivityRepository;
import com.example.demo_1.Repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private PackageRepository packageRepository;

    public void saveActivities(PackageRequest request,Long package_uuid)
    {
        List<Activity> activities = request.getActivityRequests();
        Package pack = packageRepository.findByUuid(package_uuid);
        for(Activity activity:activities)
        {
            Long activityUuid = activity.getUuid();
            Activity updatedActivity = new Activity();
            updatedActivity.setUuid(activityUuid);
            updatedActivity.setName(activity.getName());
            updatedActivity.setDescription(activity.getDescription());
            updatedActivity.setImageUrl(activity.getImageUrl());
            updatedActivity.setStartTimestamp(activity.getStartTimestamp());
            updatedActivity.setDurationMinutes(activity.getDurationMinutes());
            updatedActivity.setPackageUuid(package_uuid);
            updatedActivity.setLocationUuid(pack.getLoactionUuid());
            updatedActivity.setReservedCount(pack.getReservationCnt());
            updatedActivity.setPlaceName(activity.getPlaceName());
            activityRepository.save(updatedActivity);
        }
    }

}
