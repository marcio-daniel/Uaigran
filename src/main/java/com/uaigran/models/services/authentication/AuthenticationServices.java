package com.uaigran.models.services.authentication;

import com.uaigran.docs.erros.ErrorMessage;
import com.uaigran.exceptions.UnauthorizedException;
import com.uaigran.models.entities.User.User;
import com.uaigran.models.services.security.IJwtService;
import com.uaigran.models.services.user.IUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServices implements IAuthenticationServices{

    @Autowired
    private IUserServices _userServices;
    @Autowired
    private IJwtService _jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    public ResponseEntity<Object> authenticate(AuthenticationRequest request) throws UnauthorizedException {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(request.email,request.password);
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = _jwtService.generateToken((User) auth.getPrincipal());
            var response = new AuthenticationResponse();

            response.setToken(token);
            response.setUser_id(((User) auth.getPrincipal()).getId());

            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            throw  new UnauthorizedException("Invalid user credentials!");
        }
    }
}