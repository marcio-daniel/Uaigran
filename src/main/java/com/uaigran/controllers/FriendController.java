package com.uaigran.controllers;

import com.uaigran.docs.swagger.FriendControllerDocs;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.ConflictException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.friend.FriendRequest;
import com.uaigran.models.services.friend.IFriendServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/friend")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FriendController implements FriendControllerDocs {

    @Autowired
    private IFriendServices _friendServices;

    @PostMapping("/add")
    public ResponseEntity<Object> addFriend(@RequestBody FriendRequest request) throws ConflictException, InternalServerException, BadRequestException {
        return _friendServices.addFriend(request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> removeFriend(@RequestBody FriendRequest request) throws InternalServerException, BadRequestException {
        return _friendServices.removeFriend(request);
    }
    @GetMapping("/list/{owner_id}")
    public  ResponseEntity<Object> friendList(@PathVariable("owner_id") UUID owner_id) throws InternalServerException, BadRequestException {
        return  _friendServices.findAllFriendsByOwner(owner_id);
    }

    @GetMapping("/profiles")
    public ResponseEntity<Object> profiles() throws InternalServerException, BadRequestException {
        return _friendServices.profiles();
    }
}
