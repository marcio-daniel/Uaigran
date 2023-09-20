package com.uaigran.controllers;

import com.uaigran.docs.swagger.UserControllerDocs;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.ConflictException;
import com.uaigran.exceptions.FileProcessingException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.user.CreateUserRequest;
import com.uaigran.models.services.user.IUserServices;
import com.uaigran.models.services.user.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController implements UserControllerDocs {

    @Autowired
    private IUserServices _userServices;

    @PostMapping("/create")
    @Override
    public ResponseEntity<Object> createUser(@RequestBody CreateUserRequest request) throws ConflictException, InternalServerException {
        return _userServices.createUser(request);
    }


    @PostMapping("/profile/update")
    @Override
    public ResponseEntity<Object> updateProfile(@ModelAttribute UpdateUserRequest request) throws InternalServerException, BadRequestException, FileProcessingException {
        return _userServices.updateProfile(request);
    }


    @GetMapping("/profile/{user_id}")
    @Override
    public ResponseEntity<Object> profile(@PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException {
        return _userServices.profile(user_id);
    }
}
