package com.example.demo_1.Controller;

import com.example.demo_1.Entity.*;
import com.example.demo_1.Entity.Package;
import com.example.demo_1.Payload.Request.ReservationRequest;
import com.example.demo_1.Payload.Response.ReservationResponse;
import com.example.demo_1.Repository.*;
import com.example.demo_1.Service.ReservationService;
import com.example.demo_1.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@Transactional
@CrossOrigin
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
}