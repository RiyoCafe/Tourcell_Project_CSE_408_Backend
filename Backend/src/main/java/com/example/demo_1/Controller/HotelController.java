package com.example.demo_1.Controller;

import com.example.demo_1.Entity.Hotel;
import com.example.demo_1.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class HotelController {
    @Autowired
    private HotelRepository repository;
    @PreAuthorize("hasAnyRole('ROLE_VENDOR','ROLE_CUSTOMER','ROLE_ADMIN')")
    @GetMapping("/api/hotels")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        return new ResponseEntity<>(repository.findAll(),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/hotels")
    public ResponseEntity<Hotel> addNewHotel(@RequestBody Hotel hotel){
        Hotel newHotel = repository.save(hotel);
        return new ResponseEntity<>(newHotel,HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/hotels/{hotel_uuid}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long hotel_uuid,@RequestBody Hotel hotel)
    {
        Hotel updatedHotel = repository.findByUuid(hotel_uuid);
        updatedHotel.setName(hotel.getName());
        updatedHotel.setStar(hotel.getStar());
        updatedHotel.setLatitude(hotel.getLatitude());
        updatedHotel.setLongitude(hotel.getLongitude());
        updatedHotel.setDescription(hotel.getDescription());
        updatedHotel.setImageUrl(hotel.getImageUrl());

        return new ResponseEntity<>(repository.save(updatedHotel),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/api/hotels/{hotel_uuid}")
    public ResponseEntity<Hotel> deleteHotel(@PathVariable Long hotel_uuid)
    {
        Hotel deltedHotel = repository.findByUuid(hotel_uuid);
        repository.deleteByUuid(hotel_uuid);
        return new ResponseEntity<>(deltedHotel,HttpStatus.OK);
    }
}
