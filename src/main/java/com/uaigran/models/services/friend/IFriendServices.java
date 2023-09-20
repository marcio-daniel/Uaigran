package com.uaigran.models.services.friend;

import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.ConflictException;
import com.uaigran.exceptions.InternalServerException;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IFriendServices {
    public ResponseEntity<Object> addFriend(FriendRequest request) throws BadRequestException, InternalServerException, ConflictException;
    public ResponseEntity<Object> findAllFriendsByOwner(UUID owner_id) throws BadRequestException, InternalServerException;
    public ResponseEntity<Object> removeFriend(FriendRequest request) throws BadRequestException, InternalServerException;

    public ResponseEntity<Object> profiles() throws InternalServerException, BadRequestException;
}
