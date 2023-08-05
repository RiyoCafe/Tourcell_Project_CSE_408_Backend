package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Location;
import com.example.demo_1.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LocationController {
    @Autowired
    private LocationRepository repository;

//    @PreAuthorize("hasAnyRole('ROLE_VENDOR','ROLE_CUSTOMER','ROLE_ADMIN')")
    @GetMapping("/api/public/locations")
    public ResponseEntity<List<Location>> getAllLocations(@RequestParam(required = false) String sortBy)
    {
        List<Location> locations=new ArrayList<>();
        if(sortBy == null)  locations = repository.findAll();
        else if(sortBy.equalsIgnoreCase("SearchCnt"))  locations = repository.findTop4ByOrderBySearchCntDesc();
        else if(sortBy.equalsIgnoreCase( "ReservationCnt"))  locations = repository.findTop4ByOrderByReservationCntDesc();

        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/public/locations/{location_uuid}")
    public ResponseEntity<Location> getLocationByid(@PathVariable Long location_uuid)
    {
        return new ResponseEntity<>(repository.findByUuid(location_uuid),HttpStatus.OK);
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/public/locations/{location_city}")
    public ResponseEntity<Location> getLocationByCity(@PathVariable String location_city)
    {
        return new ResponseEntity<>(repository.findByCity(location_city),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/locations")
    public ResponseEntity<Location> addNewLocation(@RequestBody Location location)
    {
        Location newLocation = repository.save(location);
        return new ResponseEntity<>(newLocation,HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/locations/{location_uuid}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long location_uuid,@RequestBody Location location){
        Location updatedLocation = repository.findByUuid(location_uuid);
        updatedLocation.setCity(location.getCity());
        updatedLocation.setCountry(location.getCountry());
        updatedLocation.setSearchCnt(location.getSearchCnt());
        updatedLocation.setReservationCnt(location.getReservationCnt());
        return new ResponseEntity<>(repository.save(updatedLocation),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/api/locations/{location_uuid}")
    public ResponseEntity<Location> deleteLocation(@PathVariable Long location_uuid){
        Location deletedLocation = repository.findByUuid(location_uuid);
        repository.deleteByUuid(location_uuid);
        return new ResponseEntity<>(deletedLocation,HttpStatus.OK);
    }

}
