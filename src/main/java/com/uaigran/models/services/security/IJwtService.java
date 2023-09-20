package com.uaigran.models.services.security;


import com.uaigran.models.entities.User.User;

public interface IJwtService {
    String generateToken(User user);
    String validateToken(String token);
}
