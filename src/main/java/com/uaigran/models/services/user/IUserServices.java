package com.uaigran.models.services.user;

import com.uaigran.exceptions.ConflictException;
import com.uaigran.exceptions.FileProcessingException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.models.entities.User.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IUserServices {
    public ResponseEntity<Object> createUser(CreateUserRequest request) throws InternalServerException, ConflictException;

    public ResponseEntity<Object> uploadPhotoProfile(MultipartFile photo,UUID user_id) throws InternalServerException,  FileProcessingException,BadRequestException;

    public ResponseEntity<Object> updateProfile(UpdateUserRequest request) throws BadRequestException, InternalServerException, FileProcessingException;


    public ResponseEntity<Object> profile(UUID user_id) throws BadRequestException, InternalServerException;

    public UserDetails getUser(String email);
    public  User getUserById(UUID id);

    public List<User> list();

}
