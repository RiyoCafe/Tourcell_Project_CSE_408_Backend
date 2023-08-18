package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Activity;
import com.example.demo_1.Payload.Response.MessageResponse;
import com.example.demo_1.Repository.ActivityRepository;
import com.example.demo_1.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class ActivityController {
    @Autowired
    private ActivityRepository repository;
    @Autowired
    private ActivityService activityService;
    @PreAuthorize("hasAnyRole('ROLE_VENDOR','ROLE_CUSTOMER')")
    @GetMapping("/api/activities")
    public ResponseEntity<List<Activity>> getAllActivities()
    {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }
    //@PreAuthorize("hasAnyRole('ROLE_VENDOR','ROLE_CUSTOMER')")
    @GetMapping("/api/public/activities/{activity_uuid}")
    public ResponseEntity<?> getActivityByid(@PathVariable Long activity_uuid)
    {
        return new ResponseEntity<>(repository.findByUuid(activity_uuid), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @PostMapping("/api/activities/package/{package_uuid}")
    public ResponseEntity<?> addActivity(@RequestBody Activity activity,@PathVariable Long package_uuid)
    {
        Activity newActivity = activityService.singleActivitySave(activity,package_uuid);
        return new ResponseEntity<>(newActivity,HttpStatus.OK);
    }
    @PutMapping("/api/activities/package/{package_uuid}")
    public ResponseEntity<?> updateActivity( @PathVariable Long package_uuid,@RequestBody Activity activity)
    {
        Activity updatedActivity = activityService.singleActivitySave(activity, package_uuid);
        return new ResponseEntity<>(updatedActivity,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @DeleteMapping("/api/vendor/activities/{activity_uuid}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long activity_uuid)
    {
        Activity deletedActivity = activityService.deleteActivity(activity_uuid);
        return new ResponseEntity<>(deletedActivity,HttpStatus.OK);
    }
    //@PreAuthorize("hasAnyRole('ROLE_VENDOR','ROLE_CUSTOMER')")
    @GetMapping("/api/public/activities/location/{location_uuid}")
    public ResponseEntity<?> getActivityTagsByCity(@PathVariable Long location_uuid){
        List<Activity> activities = repository.findAllByLocationUuid(location_uuid);
        return new ResponseEntity<>(activities,HttpStatus.OK );
    }
    @GetMapping("/api/public/activities")
    public ResponseEntity<?> getActivitiesSortBy()
    {
        List<Activity> activities = repository.findTop4ByOrderByReservedCountDesc();
        return ResponseEntity.ok(activities);
    }

    
}
