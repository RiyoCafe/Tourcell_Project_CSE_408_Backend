package com.example.demo_1.Service;

import com.example.demo_1.Entity.*;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.ReservationRequest;
import com.example.demo_1.Payload.Response.ReservationResponse;
import com.example.demo_1.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationChoiceRepository reservationChoiceRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private FlightOptionsRepository flightOptionsRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private HotelPackageOptionsRepository hotelPackageOptionsRepository;
    @Autowired
    private  HotelPackageRepository hotelPackageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    public ReservationResponse makeSingleReservationResponse(Reservation reservation,Long customerUuid)
    {
        Long packageUuid = reservation.getPackageUuid();
        Long reservationUuid = reservation.getUuid();
        Package package_= packageRepository.findByUuid(packageUuid);
        User vendor = userRepository.findByUuid(package_.getVendorUuid());
        Location location = locationRepository.findByUuid(package_.getLoactionUuid());
        List<ReservationChoice> choices = reservationChoiceRepository.findAllByReservationUuid(reservationUuid);

        List<String> choiceStrings = new ArrayList<>();
        for(ReservationChoice choice:choices)
        {
            if(choice.getChoiceType()==ChoiceType.FLIGHT_OPTION)
            {
                String flightString = getFlightString(choice);
                choiceStrings.add(flightString);
            }
            else {
                String hotelString = getHotelString(choice);
                choiceStrings.add(hotelString);
            }
        }
        ReservationResponse response = new ReservationResponse();
        response.setReservationUuid(reservationUuid);
        response.setCustomerUuid(customerUuid);
        response.setPackageUuid(packageUuid);
        response.setPackageName(package_.getName());
        response.setLocationName(location.getCity()+","+location.getCountry());
        response.setDurationDays(package_.getDurationDays());
        response.setStartTimestamp(package_.getStartTimestamp());
        response.setVendorName(vendor.getFirstname()+" "+vendor.getLastname());
        response.setTotalCost(reservation.getTotalCost());
        response.setReservationChoices(choiceStrings);
        return response;
    }

    private String getHotelString(ReservationChoice choice) {
        String hotelString = new String();
        HotelPackageOptions options = hotelPackageOptionsRepository.findByUuid(choice.getChoiceUuid());
        HotelPackage hotelPackage = hotelPackageRepository.findByUuid(options.getHotelPackageUuid());
        Hotel hotel = hotelRepository.findByUuid(hotelPackage.getHotelUuid());
        hotelString+="Stay in "+hotel.getName();
        LocalDateTime localDateTime = hotelPackage.getStartTimestamp().toLocalDateTime();
        hotelString+=" from "+localDateTime.toLocalDate()+","+localDateTime.getHour()+":"+localDateTime.getMinute()+" for "+hotelPackage.getDurationMinutes()+" minutues"+".";
        if(options.getAirConditioned()){
            hotelString+="Air conditioned room"+",";
        }
        if(options.getBreakfastProvided()){
            hotelString+="Breakfast"+".";
        }
        return hotelString;
    }

    private String getFlightString(ReservationChoice choice) {
        String flightString=new String();
        FlightOptions options = flightOptionsRepository.findByUuid(choice.getChoiceUuid());
        Flight flight = flightRepository.findByUuid(options.getFlightUuid());
        Location start = locationRepository.findByUuid(flight.getStartLocationUuid());
        Location end = locationRepository.findByUuid(flight.getEndLocationUuid());
        flightString+=("Starting from "+start.getCity()+","+start.getCountry());
        flightString+=(" To "+ end.getCity()+","+end.getCountry());
        flightString+=(" with "+flight.getAirlinesNames()+" airlines"+",");

        if(options.getFoodProvided()){
            flightString += "Food included"+",";
        }
        else{
            flightString+="Food not included"+",";
        }
        if(options.getBusinessClass()){
            flightString+="Business Class"+".";
        }
        else{
            flightString+="Economy Class"+".";
        }
        return flightString;
    }

    public List<ReservationResponse> makeResponse(Long uuid)
    {
        List<ReservationResponse> responseList=new ArrayList<>();
        Long customerUuid = userService.getMyUserUuid();
        List<Reservation> reservations = reservationRepository.findAllByCustomerUuid(uuid);

        for(Reservation reservation:reservations)
        {
            ReservationResponse response = makeSingleReservationResponse(reservation,customerUuid);
            responseList.add(response);
        }
        return responseList;
    }

    public Reservation addNewReservation(ReservationRequest request) {
        Long packageUuid = request.getPackageUuid();
        Package savedPackage=packageRepository.findByUuid(packageUuid);
        savedPackage.setReservationCnt(savedPackage.getReservationCnt()+1);
        packageRepository.save(savedPackage);
        Reservation reservation = new Reservation();
        reservation.setCustomerUuid(request.getCustomerUuid());
        reservation.setPackageUuid(packageUuid);
        reservation.setTotalCost(request.getTotalCost());
        Reservation savedReservation = reservationRepository.save(reservation);
        List<ReservationChoice> choices = request.getReservationChoices();
        List<ReservationChoice> responseChoices = new ArrayList<>();
        for(ReservationChoice choice:choices) {
            choice.setReservationUuid(savedReservation.getUuid());
            responseChoices.add(choice);
            reservationChoiceRepository.save(choice);
        }
        return savedReservation;
    }
}