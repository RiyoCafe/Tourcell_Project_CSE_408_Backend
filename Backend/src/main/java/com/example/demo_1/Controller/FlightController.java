package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Flight;
import org.springframework.stereotype.Controller;
import com.example.demo_1.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FlightController {
    @Autowired
    private FlightRepository repository;
    @GetMapping("/api/flights")
    public ResponseEntity<List<Flight>> getAllFlights()
    {
        return new ResponseEntity<>(repository.findAll(),HttpStatus.OK);
    }
    @GetMapping("/api/flights/{package_uuid}")
    public ResponseEntity<?> getAllFlights(@PathVariable Long package_uuid)
    {
        return new ResponseEntity<>(repository.findAllByPackageUuid(package_uuid),HttpStatus.OK);
    }
    @PostMapping("/api/flights")
    public ResponseEntity<Flight>   addFlight(@RequestBody Flight flight){
        Flight newFlight=repository.save(flight);
        return new ResponseEntity<>(newFlight,HttpStatus.CREATED);
    }
    @PutMapping("/api/flights/{flight_uuid}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long flight_uuid,@RequestBody Flight flight)
    {
        Flight updatedFlight=repository.findByUuid(flight_uuid);
        updatedFlight.setDescription(flight.getDescription());
        updatedFlight.setChangePrice(flight.getChangePrice());
        updatedFlight.setAirlinesNames(flight.getAirlinesNames());
        updatedFlight.setImageUrl(flight.getImageUrl());
        updatedFlight.setDurationMinutes(flight.getDurationMinutes());
        updatedFlight.setStartLocationUuid(flight.getStartLocationUuid());
        updatedFlight.setEndLocationUuid(flight.getEndLocationUuid());
        updatedFlight.setStartTimestamp(flight.getStartTimestamp());
        return new ResponseEntity<>(repository.save(updatedFlight),HttpStatus.OK);
    }
    @DeleteMapping("/api/flights/{flight_uuid}")
    public ResponseEntity<Flight> deleteFlight(@PathVariable Long flight_uuid)
    {
        Flight deletedFlight = repository.findByUuid(flight_uuid);
        repository.deleteByUuid(flight_uuid);
        return new ResponseEntity<>(deletedFlight,HttpStatus.OK);
    }

}
