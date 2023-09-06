package com.example.demo_1.Controller;

import com.example.demo_1.Entity.User;
import com.example.demo_1.Repository.UserRepository;
import com.example.demo_1.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserService service;
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllEmployees()
    {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }
    @PostMapping("/api/users")
    public ResponseEntity<User> addUser(@RequestBody User user)
    {
        User newuser=repository.save(user);
        return new ResponseEntity<>(newuser,HttpStatus.CREATED);
    }
    @PutMapping("api/user")
    public ResponseEntity<User> updateUser( @RequestBody User user)
    {
        Long user_uuid=service.getMyUserUuid();
        User updatedUser=repository.findByUuid(user_uuid);
        updatedUser.setFirstname(user.getFirstname());
        updatedUser.setLastname(user.getLastname());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        return new ResponseEntity<>(repository.save(updatedUser),HttpStatus.OK);
    }
    


}
