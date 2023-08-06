package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Flight;
import com.example.demo_1.Payload.Request.FlightDetailsRequest;
import com.example.demo_1.Payload.Response.FlightDetailsResponse;
import com.example.demo_1.Payload.Response.MessageResponse;
import com.example.demo_1.Service.FlightDetailsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.example.demo_1.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class FlightController {
    @Autowired
    private FlightRepository repository;
    @Autowired
    private FlightDetailsService flightDetailsService;

    @GetMapping("/api/package/{package_uuid}/flights")
    public ResponseEntity<?> getAllFlights(@PathVariable Long package_uuid)
    {
        List<FlightDetailsResponse> responses = flightDetailsService.getFlightDetails(package_uuid);
        return ResponseEntity.ok(responses);
    }
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @PostMapping("/api/vendor/flights/{package_uuid}")
    public ResponseEntity<?> addFlight(@RequestBody FlightDetailsRequest request, @PathVariable Long package_uuid){
        FlightDetailsResponse response = flightDetailsService.saveFlightAndFlightOptionsByPackageUuid(request, package_uuid,request.getFlightUuid());
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @PutMapping("/api/vendor/package/{package_uuid}/flights/{flight_uuid}")
    public ResponseEntity<?> updateFlight(@PathVariable Long flight_uuid,@RequestBody FlightDetailsRequest request,@PathVariable Long package_uuid)
    {
        FlightDetailsResponse response = flightDetailsService.saveFlightAndFlightOptionsByPackageUuid(request,package_uuid,flight_uuid);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @DeleteMapping("/api/vendor/flights/{flight_uuid}")
    public ResponseEntity<?> deleteFlight(@PathVariable Long flight_uuid)
    {
        FlightDetailsResponse response = flightDetailsService.deleteFlightAndFlightOptions(flight_uuid);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
