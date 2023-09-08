package com.example.demo_1.Service;

import com.example.demo_1.Entity.*;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.ReservationRequest;
import com.example.demo_1.Payload.Response.DetailsResponseOfParticularReservation;
import com.example.demo_1.Payload.Response.ReservationResponse;
import com.example.demo_1.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
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
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private NotificationService notificationService;
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
            else if(choice.getChoiceType() == ChoiceType.HOTEL_PACKAGE_OPTION){
                String hotelString = getHotelString(choice);
                choiceStrings.add(hotelString);
            }
            else{
                String activityString = getActivityString(choice);
                choiceStrings.add(activityString);
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
            hotelString+="Breakfast"+",";
        }
        if(options.getLunchProvided()){
            hotelString+="Lunch,";
        }
        if(options.getSwimmingPoolProvided()){
            hotelString+="Swimming pool,";
        }
        if(options.getFreeWifiProvided()){
            hotelString+="FreeWifi,";
        }
        if(options.getParkingProvided()){
            hotelString+="Parking,";
        }
        if(options.getMassageProvided()){
            hotelString+="Massage,";
        }
        if(options.getRoomCleanProvided()){
            hotelString+="Room clean,";
        }
        if(options.getFitnessCenterProvided()){
            hotelString+="Fitness Center,";
        }
        if(options.getBarProvided()){
            hotelString+="Bar,";
        }
        if(options.getLaundryProvided()){
            hotelString+="Laundry,";
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

    private String getActivityString(ReservationChoice choice){
        Activity choosenActivity = activityRepository.findByUuid(choice.getChoiceUuid());
        String activityString = new String();
        activityString+="intend to do "+choosenActivity.getName()+" activity in "+choosenActivity.getPlaceName()+" for taka "+
                choosenActivity.getActivityPrice()+" only.";
        return activityString;
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
        //reservation.setTimestamp(Timestamp.from(Instant.now()));
        reservation.setTimestamp(request.getTimestamp());
        reservation.setVendorUuid(savedPackage.getVendorUuid());

        Reservation savedReservation = reservationRepository.save(reservation);

        User vendor = userRepository.findByUuid(savedPackage.getVendorUuid());

        notificationService.makeNotification(request.getCustomerUuid(), vendor.getUuid(), NotificationType.BOOKING, savedPackage.getName());

        List<ReservationChoice> choices = request.getReservationChoices();
        List<ReservationChoice> responseChoices = new ArrayList<>();
        for(ReservationChoice choice:choices) {
            choice.setReservationUuid(savedReservation.getUuid());
            responseChoices.add(choice);
            reservationChoiceRepository.save(choice);
        }
        return savedReservation;
    }

    public DetailsResponseOfParticularReservation makeDetailsReservationResponse(Long reservationUuid) {
        Reservation reservation = reservationRepository.findByUuid(reservationUuid);
        Package pack = packageRepository.findByUuid(reservation.getPackageUuid());
        User vendor = userRepository.findByUuid(reservation.getVendorUuid());
        User customer = userRepository.findByUuid(reservation.getCustomerUuid());
        List<Activity> activities = new ArrayList<>();
        DetailsResponseOfParticularReservation response = new DetailsResponseOfParticularReservation();
        response.setTotalCost(reservation.getTotalCost());
        response.setPackageName(pack.getName());
        response.setCustomerName(customer.getFirstname() + " " + customer.getLastname());
        response.setCustomerEmail(customer.getEmail());
        response.setVendorName(vendor.getFirstname() + " " + vendor.getLastname());
        response.setVendorEmail(vendor.getEmail());
        response.setStartTimeOfTour(reservation.getTimestamp());

        List<ReservationChoice> choices = reservationChoiceRepository.findAllByReservationUuid(reservationUuid);
        for (ReservationChoice reserve : choices) {
            if (reserve.getChoiceType() == ChoiceType.HOTEL_PACKAGE_OPTION) {
                HotelPackageOptions hotelPackageOptions = hotelPackageOptionsRepository.findByUuid(reserve.getChoiceUuid());
                HotelPackage hotelPackage = hotelPackageRepository.findByUuid(hotelPackageOptions.getHotelPackageUuid());
                Hotel hotel = hotelRepository.findByUuid(hotelPackage.getHotelUuid());
                response.setHotelName(hotel.getName());
                response.setHotelPackageOptions(hotelPackageOptions);
            }
            if (reserve.getChoiceType() == ChoiceType.FLIGHT_OPTION) {
                FlightOptions flightOptions = flightOptionsRepository.findByUuid(reserve.getChoiceUuid());
                Flight flight = flightRepository.findByUuid(flightOptions.getFlightUuid());
                response.setFlightName(flight.getAirlinesNames());
                response.setFlightOptions(flightOptions);
            }
            if (reserve.getChoiceType() == ChoiceType.ACTIVITY) {
                Activity activity = activityRepository.findByUuid(reserve.getChoiceUuid());
                activities.add(activity);
            }
        }
        response.setActivityList(activities);
        return response;
    }
}
