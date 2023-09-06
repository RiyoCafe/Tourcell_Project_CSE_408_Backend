package com.example.demo_1.Payload.Request;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    String firstname;
    String lastname;
    String email;
    Set<String> role;
    String password;
    String emergencyContactNo;
}
