package com.uaigran.controllers;

import com.uaigran.docs.swagger.AuthenticationControllerDocs;
import com.uaigran.exceptions.UnauthorizedException;
import com.uaigran.models.services.authentication.AuthenticationRequest;
import com.uaigran.models.services.authentication.AuthenticationResponse;
import com.uaigran.models.services.authentication.IAuthenticationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController implements AuthenticationControllerDocs {

    @Autowired
    private IAuthenticationServices _authenticateServices;

    @PostMapping()
    @Override
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) throws UnauthorizedException {
        return _authenticateServices.authenticate(request);
    }
}
