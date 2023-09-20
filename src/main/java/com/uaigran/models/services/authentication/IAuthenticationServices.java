package com.uaigran.models.services.authentication;

import com.uaigran.exceptions.UnauthorizedException;
import org.springframework.http.ResponseEntity;

public interface IAuthenticationServices {
    ResponseEntity<Object> authenticate(AuthenticationRequest request) throws UnauthorizedException;
}
