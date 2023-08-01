package com.example.demo_1.Service;

import com.example.demo_1.Entity.Flight;
import com.example.demo_1.Entity.FlightOptions;
import com.example.demo_1.Payload.Request.FlightDetailsRequest;
import com.example.demo_1.Payload.Request.PackageRequest;
import com.example.demo_1.Payload.Response.FlightDetailsResponse;
import com.example.demo_1.Payload.Response.MessageResponse;
import com.example.demo_1.Repository.FlightOptionsRepository;
import com.example.demo_1.Repository.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FlightDetailsService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private FlightOptionsRepository flightOptionsRepository;

    private FlightDetailsResponse makeResponse(Flight flight){
        List<FlightOptions> options = flightOptionsRepository.findAllByFlightUuid(flight.getUuid());
        FlightDetailsResponse response=new FlightDetailsResponse();
        response.setFlightUuid(flight.getUuid());
        response.setAirlinesNames(flight.getAirlinesNames());
        response.setStartLocationUuid(flight.getStartLocationUuid());
        response.setEndLocationUuid(flight.getEndLocationUuid());
        response.setStartTimestamp(flight.getStartTimestamp());
        response.setImageUrl(flight.getImageUrl());
        response.setDurationMinutes(flight.getDurationMinutes());
        response.setChangePrice(flight.getChangePrice());
        response.setOptionsList(options);
        response.setDescription(flight.getDescription());
        return response;
    }
    private Flight createFlightOrUpdate(FlightDetailsRequest flightDetailsRequest, Long packageUuid,Long uuid) {
        Flight flight=new Flight();
        if(uuid!=null)  flight.setUuid(uuid);
        flight.setAirlinesNames(flightDetailsRequest.getAirlinesNames());
        flight.setStartLocationUuid(flightDetailsRequest.getStartLocationUuid());
        flight.setEndLocationUuid(flightDetailsRequest.getEndLocationUuid());
        flight.setStartTimestamp(flightDetailsRequest.getStartTimestamp());
        flight.setImageUrl(flightDetailsRequest.getImageUrl());
        flight.setDurationMinutes(flightDetailsRequest.getDurationMinutes());
        flight.setPackageUuid(packageUuid);
        flight.setDescription(flightDetailsRequest.getDescription());
        flight.setChangePrice(flightDetailsRequest.getChangePrice());
        flight.setDescription(flightDetailsRequest.getDescription());
        Flight savedFlight=flightRepository.save(flight);
        return savedFlight;
    }
    public List<FlightDetailsResponse> getFlightDetails(Long packageUuid){
        List<FlightDetailsResponse> responses= new ArrayList<>();
        List<Flight> flightList = flightRepository.findAllByPackageUuid(packageUuid);
        for(Flight flight:flightList){
            responses.add(makeResponse(flight));
        }
        return responses;
    }
    private void onlyFlightOptionsAddOrUpdate(FlightOptions option,Long flightUuid)
    {
        Long flightOptionUuid = option.getUuid();
        FlightOptions updatedOption = new FlightOptions();
        if(flightOptionUuid!=null)  updatedOption.setUuid(flightOptionUuid);
        updatedOption.setChangePrice(option.getChangePrice());
        updatedOption.setBusinessClass(option.getBusinessClass());
        updatedOption.setFoodProvided(option.getFoodProvided());
        updatedOption.setFlightUuid(flightUuid);
        flightOptionsRepository.save(updatedOption);
    }
    private FlightDetailsResponse middleWareFunctionAddOrUpdate(FlightDetailsRequest flightDetailsRequest, Long packageUuid,Long FlightUuid)
    {
        Flight savedFlight = createFlightOrUpdate(flightDetailsRequest,packageUuid,FlightUuid);
        Long flightUuid= savedFlight.getUuid();
        List<FlightOptions> options= flightDetailsRequest.getOptionsList();
        
        for(FlightOptions option:options){
            onlyFlightOptionsAddOrUpdate(option,flightUuid);
        }
        FlightDetailsResponse response = makeResponse(savedFlight);
        return response;
    }
    //packageUuid will come from packageController
//    public void addFlightAndFlightOptions(PackageRequest packageRequest, Long packageUuid){
//        List<FlightDetailsRequest> flightDetailsRequests = packageRequest.getFlightDetailsRequests();
//        for(FlightDetailsRequest flightDetailsRequest:flightDetailsRequests){
//            middleWareFunctionAddOrUpdate(flightDetailsRequest,packageUuid,flightDetailsRequest.getFlightUuid());
//        }
//    }
    //existing package and add a new flight and flightOptions
//    public void addFlightAndFlightOptionsByPackageUuid(FlightDetailsRequest request,Long packageUuid)
//    {
//        middleWareFunctionAddOrUpdate(request,packageUuid, request.getFlightUuid());
//    }

    public String saveFlightAndFlightOptions(PackageRequest packageRequest, Long packageUuid){
        List<FlightDetailsRequest> flightDetailsRequests = packageRequest.getFlightDetailsRequests();
        for(FlightDetailsRequest request:flightDetailsRequests){
            middleWareFunctionAddOrUpdate(request,packageUuid, request.getFlightUuid());
        }
        return  "saved flight and flightOptions";

    }
    //existing package and update a new flight and flightOptions
    public FlightDetailsResponse saveFlightAndFlightOptionsByPackageUuid(FlightDetailsRequest request,Long packageUuid,Long flightUuid)
    {
        return middleWareFunctionAddOrUpdate(request,packageUuid,flightUuid);

    }

    public FlightDetailsResponse deleteFlightAndFlightOptions(Long flightUuid)
    {
        Flight flight = flightRepository.findByUuid(flightUuid);
        FlightDetailsResponse response = makeResponse(flight);
        List<FlightOptions> options = flightOptionsRepository.findAllByFlightUuid(flightUuid);

        System.out.println("joya "+options);
        for(FlightOptions option:options)
        {
            flightOptionsRepository.deleteByUuid(option.getUuid());
        }
        flightRepository.deleteByUuid(flightUuid);

        return response;
    }
    public List<FlightDetailsResponse> deleteFlightAndFlightOptionsByPackageUuid(Long packageUuid)
    {
        List<FlightDetailsResponse> responses = new ArrayList<>();
        List<Flight> flights = flightRepository.findAllByPackageUuid(packageUuid);
        for(Flight flight:flights)
        {
            responses.add(deleteFlightAndFlightOptions(flight.getUuid()));
        }
        return responses;
    }



}
