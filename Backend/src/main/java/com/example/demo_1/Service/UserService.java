package com.example.demo_1.Service;

import com.example.demo_1.Entity.AuthProvider;
import com.example.demo_1.Entity.ERole;
import com.example.demo_1.Entity.Role;
import com.example.demo_1.Payload.Request.OauthGoogleSignInRequest;
import com.example.demo_1.Payload.Response.JwtResponse;
import com.example.demo_1.Repository.RoleRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import com.example.demo_1.Entity.User;
import com.example.demo_1.Repository.UserRepository;
import com.example.demo_1.Security.jwt.JwtUtils;
import com.example.demo_1.Security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;


    public Long getMyUserUuid()
    {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUuid();
    }

    public JwtResponse loginOAuthGoogle(OauthGoogleSignInRequest request) {
        User user = verifyGoogleIDToken(request);
        if (user == null) {
            throw new IllegalArgumentException();
        }
        return authenticateAndCreateJWT(user.getEmail(), user.getPassword());
    }
    ///similar as authentication in signin
    private User verifyGoogleIDToken(OauthGoogleSignInRequest request) {
        try {
            GoogleIdToken idTokenObj = jwtUtils.verifyGoogleOauthToken(request.getIdToken());
            if (idTokenObj == null) {
                return null;
            }
            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String email = payload.getEmail();
            User user = new User();
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setEmail(email);
            user.setPassword("Google");
            user.setProvider(AuthProvider.GOOGLE);
            Set<String> strRoles =request.getRole();
            Set<Role> roles = getUserRoles(strRoles);
            user.setRoles(roles);

            User savedUser = userRepository.save(user);


            return savedUser;
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
    public Set<Role> getUserRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "vendor":
                        Role modRole = roleRepository.findByName(ERole.ROLE_VENDOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }
    public JwtResponse authenticateAndCreateJWT(String email, String password)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getUuid(),
                userDetails.getEmail(),
                roles,userDetails.getEmergencyContactNo());
    }
}
