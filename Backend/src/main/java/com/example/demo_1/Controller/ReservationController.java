package com.example.demo_1.Controller;

import com.example.demo_1.Entity.*;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.ReservationRequest;
import com.example.demo_1.Payload.Response.ReservationResponse;
import com.example.demo_1.Repository.*;
import com.example.demo_1.Service.ReservationService;
import com.example.demo_1.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Controller
@Transactional
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationChoiceRepository reservationChoiceRepository;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/api/reservations")
    public ResponseEntity<?> getAllReservations()
    {
        List<ReservationResponse> responseList = reservationService.makeResponse(userService.getMyUserUuid());
        return ResponseEntity.ok(responseList);
    }
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/api/reservations")
    public ResponseEntity<?> addNewReservation(@RequestBody ReservationRequest request)
    {
        Reservation newReservation = reservationService.addNewReservation(request);
        ReservationResponse response = reservationService.makeSingleReservationResponse(newReservation, userService.getMyUserUuid());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @GetMapping("/api/vendor/past-reservations")
    public ResponseEntity<?> getAllPrevReservationsOfVendor()
    {
        Long vendorUuid = userService.getMyUserUuid();
        List<Reservation> pastReservations = reservationRepository.findAllByTimestampBeforeAndVendorUuid(Timestamp.from(Instant.now()),vendorUuid);
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for (Reservation reservation:pastReservations){
            reservationResponses.add(reservationService.makeSingleReservationResponse(reservation, reservation.getCustomerUuid()));
        }
        return ResponseEntity.ok(reservationResponses);
    }

    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @GetMapping("/api/vendor/upcoming-reservations")
    public ResponseEntity<?> getAllUpcomingReservationsOfVendor()
    {
        Long vendorUuid = userService.getMyUserUuid();
        List<Reservation> upcomingReservations = reservationRepository.findAllByTimestampAfterAndVendorUuid(Timestamp.from(Instant.now()),vendorUuid);
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for (Reservation reservation:upcomingReservations){
            reservationResponses.add(reservationService.makeSingleReservationResponse(reservation, reservation.getCustomerUuid()));
        }
        return ResponseEntity.ok(reservationResponses);
    }
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/api/customer/past-reservations")
    public ResponseEntity<?> getAllPrevReservationsOfCustomer()
    {
        Long customerUuid = userService.getMyUserUuid();
        List<Reservation> pastReservations = reservationRepository.findAllByTimestampBeforeAndCustomerUuid(Timestamp.from(Instant.now()),customerUuid);
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for (Reservation reservation:pastReservations){
            reservationResponses.add(reservationService.makeSingleReservationResponse(reservation, reservation.getCustomerUuid()));
        }
        return ResponseEntity.ok(reservationResponses);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/api/customer/upcoming-reservations")
    public ResponseEntity<?> getAllUpcomingReservationsOfCustomer()
    {
        Long customerUuid = userService.getMyUserUuid();
        List<Reservation> pastReservations = reservationRepository.findAllByTimestampAfterAndCustomerUuid(Timestamp.from(Instant.now()),customerUuid);
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for (Reservation reservation:pastReservations){
            reservationResponses.add(reservationService.makeSingleReservationResponse(reservation, reservation.getCustomerUuid()));
        }
        return ResponseEntity.ok(reservationResponses);
    }

}
