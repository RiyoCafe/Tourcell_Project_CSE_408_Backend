package com.example.demo_1.Service;

import com.example.demo_1.Entity.Flight;
import com.example.demo_1.Entity.FlightOptions;
import com.example.demo_1.Payload.Request.FlightDetailsRequest;
import com.example.demo_1.Payload.Request.PackageRequest;
import com.example.demo_1.Payload.Response.FlightDetailsResponse;
import com.example.demo_1.Repository.FlightOptionsRepository;
import com.example.demo_1.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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
    private Flight createFlight(FlightDetailsRequest flightDetailsRequest, Long packageUuid,Long uuid) {
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
        return flight;
    }
    public List<FlightDetailsResponse> getFlightDetails(Long packageUuid){
        List<FlightDetailsResponse> responses= new ArrayList<>();
        List<Flight> flightList = flightRepository.findAllByPackageUuid(packageUuid);
        for(Flight flight:flightList){
            responses.add(makeResponse(flight));
        }
        return responses;
    }

    public void addFlightAndFlightOptions(PackageRequest packageRequest, Long packageUuid){
        List<FlightDetailsRequest> flightDetailsRequests = packageRequest.getFlightDetailsRequests();
        for(FlightDetailsRequest flightDetailsRequest:flightDetailsRequests){
            Flight flight = createFlight(flightDetailsRequest,packageUuid,null);
            Flight savedFlight=flightRepository.save(flight);
            Long flightUuid= savedFlight.getUuid();
            List<FlightOptions> options= flightDetailsRequest.getOptionsList();
            for(FlightOptions option:options){
                option.setFlightUuid(flightUuid);
                flightOptionsRepository.save(option);
            }
        }
    }

    public String saveFlightAndFlightOptions(PackageRequest packageRequest, Long packageUuid){
        List<FlightDetailsRequest> flightDetailsRequests = packageRequest.getFlightDetailsRequests();
        for(FlightDetailsRequest request:flightDetailsRequests){
            Long flightUuid = request.getFlightUuid();
            Flight updatedOrNewFlight;
            updatedOrNewFlight = createFlight(request,packageUuid,flightUuid);
            Flight savedFlight = flightRepository.save(updatedOrNewFlight);
            flightUuid = savedFlight.getUuid();
            List<FlightOptions> options = request.getOptionsList();

            for(FlightOptions option:options){
                Long flightOptionUuid = option.getUuid();
                FlightOptions updatedOption = new FlightOptions();
                if(flightOptionUuid!=null)  updatedOption.setUuid(flightOptionUuid);
                updatedOption.setChangePrice(option.getChangePrice());
                updatedOption.setBusinessClass(option.getBusinessClass());
                updatedOption.setFoodProvided(option.getFoodProvided());
                updatedOption.setFlightUuid(flightUuid);
                flightOptionsRepository.save(updatedOption);
            }
        }
        return  "saved flight and flightOptions";

    }



}
