package com.example.demo_1.Payload.Response;

import com.example.demo_1.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    String token;
    Long uuid;
    String userName;
    String email;
    List<String> roles;
    String emergencyContactNo;

}
