package com.example.demo_1.Controller;



import com.example.demo_1.Entity.Role;
import com.example.demo_1.Entity.User;
import com.example.demo_1.Payload.Request.LoginRequest;
import com.example.demo_1.Payload.Request.OauthGoogleSignInRequest;
import com.example.demo_1.Payload.Request.SignupRequest;
import com.example.demo_1.Payload.Response.MessageResponse;
import com.example.demo_1.Repository.RoleRepository;
import com.example.demo_1.Repository.UserRepository;
import com.example.demo_1.Security.jwt.JwtUtils;
import com.example.demo_1.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {
        //
        return ResponseEntity.ok(userService.authenticateAndCreateJWT(loginRequest.getEmail(),loginRequest.getPassword()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User();
        user.setFirstname(signUpRequest.getFirstname());
        user.setLastname(signUpRequest.getLastname());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles =userService.getUserRoles(strRoles);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/oauth/google/signin")
    public ResponseEntity<?> signInWithGoogle(@RequestBody OauthGoogleSignInRequest request)
    {
        return ResponseEntity.ok(userService.loginOAuthGoogle(request));
    }

}
