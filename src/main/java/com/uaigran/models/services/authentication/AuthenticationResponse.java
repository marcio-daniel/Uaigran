package com.uaigran.models.services.authentication;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthenticationResponse {

    public UUID user_id;
    public String token;
}
