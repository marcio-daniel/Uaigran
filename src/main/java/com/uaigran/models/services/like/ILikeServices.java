package com.uaigran.models.services.like;

import com.uaigran.exceptions.InternalServerException;
import com.uaigran.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ILikeServices {
    public ResponseEntity<Object> createLike(CreateLikeRequest request) throws BadRequestException, InternalServerException;

    public ResponseEntity<Object> deleteLike(UUID like_id) throws BadRequestException, InternalServerException;

    public ResponseEntity<Object> getLike(UUID post_id,UUID user_id) throws BadRequestException, InternalServerException;
}
