package com.example.demo_1.Payload.Request;

import lombok.*;

import java.util.Set;
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OauthGoogleSignInRequest {
    String idToken;
    Set<String > role;
}
