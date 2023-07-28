package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Activity;
import com.example.demo_1.Repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ActivityController {
    @Autowired
    private ActivityRepository repository;
    @GetMapping("/api/activities")
    public ResponseEntity<List<Activity>> getAllActivities()
    {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/api/activities/{activity_uuid}")
    public ResponseEntity<Activity> getActivityByid(@PathVariable Long activity_uuid)
    {
        return new ResponseEntity<>(repository.findByUuid(activity_uuid), HttpStatus.OK);
    }
    @PostMapping("/api/activities")
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activity)
    {
        Activity newActivity = repository.save(activity);
        return new ResponseEntity<>(newActivity,HttpStatus.CREATED);
    }
    @PutMapping("/api/activities/{activity_uuid}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long activity_uuid, @RequestBody Activity activity)
    {
        Activity updatedActivity = repository.findByUuid(activity_uuid);
        updatedActivity.setDescription(activity.getDescription());
        updatedActivity.setName(activity.getName());
        updatedActivity.setImageUrl(activity.getImageUrl());
        updatedActivity.setStartTimestamp(activity.getStartTimestamp());
        updatedActivity.setDurationMinutes(activity.getDurationMinutes());
        return new ResponseEntity<>(repository.save(updatedActivity),HttpStatus.OK);
    }
    @DeleteMapping("/api/activities/{activity_uuid}")
    public ResponseEntity<Activity> deleteActivity(@PathVariable Long activity_uuid)
    {
        Activity deletedActivity=repository.findByUuid(activity_uuid);
        if(deletedActivity==null)   return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        repository.deleteByUuid(activity_uuid);
        return new ResponseEntity<>(deletedActivity,HttpStatus.OK);
    }

    @GetMapping("/api/activities/{location_uuid}")
    public ResponseEntity<?> getActivityTagsByCity(@PathVariable Long location_uuid){
        List<Activity> activities = repository.findAllByLocationUuid(location_uuid);
        return new ResponseEntity<>(activities,HttpStatus.OK );
    }

    
}
