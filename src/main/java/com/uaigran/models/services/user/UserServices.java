package com.uaigran.models.services.user;

import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.ConflictException;
import com.uaigran.exceptions.FileProcessingException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.entities.User.User;
import com.uaigran.models.repository.IUserRepository;
import com.uaigran.models.services.fileUpload.IFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
public class UserServices implements IUserServices{

    @Autowired
    private IUserRepository _userRepository;

    @Autowired
    private IFileUploadService _fileUploadServices;
    @Override
    public ResponseEntity<Object> createUser(CreateUserRequest request) throws InternalServerException, ConflictException {
    try {
        var user = User.builder()
                .name(request.name)
                .email(request.email)
                .id(UUID.randomUUID())
                .description(null)
                .role(request.role)
                .build();
        if (_userRepository.findByEmail(user.getEmail()) != null){
            throw new ConflictException("There is already a user with the email address provided!");
        }

        var hash = new BCryptPasswordEncoder().encode(request.password);
        user.setPassword(hash);
        _userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
    } catch (ConflictException e){
        throw new ConflictException(e.getMessage());
    } catch (Exception e){
        throw new InternalServerException(e.getMessage());
    }
}

    @Override
    public ResponseEntity<Object> uploadPhotoProfile(MultipartFile photo,UUID user_id) throws BadRequestException, FileProcessingException, InternalServerException {
    try {
        var user = getUserById(user_id);
        if(user == null){
            throw new BadRequestException("No user with these credentials was found!");
        }
        var photoUri = "";

        try {
            var fileName = user.getId() + "." + photo
                    .getOriginalFilename()
                    .substring(
                            photo.getOriginalFilename()
                                    .lastIndexOf(".") + 1);

            photoUri = _fileUploadServices.upload(photo, fileName);

        } catch (Exception e) {
            throw  new FileProcessingException("There was an error processing the image!");
        }

        user.setPhotoUri(photoUri);
        _userRepository.save(user);
        return ResponseEntity.ok().body("Profile picture uploaded successfully!");

    } catch (BadRequestException e){
        throw new BadRequestException(e.getMessage());
    } catch (FileProcessingException e){
        throw new FileProcessingException(e.getMessage());
    } catch (Exception e){
        throw new InternalServerException(e.getMessage());
    }
}

    @Override
    public ResponseEntity<Object> updateProfile(UpdateUserRequest request) throws BadRequestException, InternalServerException, FileProcessingException {
        try{
            if(_userRepository.findById(request.id).isEmpty()){
                throw new BadRequestException("No user with these credentials was found!");
            }
            var user = _userRepository.findById(request.id).get();
            user.setName(request.name);
            user.setDescription(request.description);
            _userRepository.save(user);
            if(request.photo != null){
                try{
                    uploadPhotoProfile(request.photo,user.getId());

                } catch (FileProcessingException e){
                    throw new FileProcessingException(e.getMessage());
                }

            }
            return ResponseEntity.ok().body("User successfully updated!");
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (FileProcessingException e){
            throw new FileProcessingException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public UserDetails getUser(String email){
        try{
            var user =_userRepository.findByEmail(email);
            return user;
        }catch (Exception e){
            return null;
        }
    }

    public User getUserById(UUID id){
        try {
            var user = _userRepository.findById(id).get();
            return user;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public List<User> list() {
        return _userRepository.findAll();
    }

    @Override
    public ResponseEntity<Object> profile(UUID user_id) throws BadRequestException, InternalServerException {
        try{
            if(_userRepository.findById(user_id).isEmpty()){
                throw new BadRequestException("No user with these credentials was found!");
            }
            var user = getUserById(user_id);
            var response = UserResponse
                    .builder()
                    .photoUri(user.getPhotoUri())
                    .name(user.getName())
                    .id(user.getId())
                    .description(user.getDescription())
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

}
