package com.uaigran.models.services.comment;

import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.InternalServerException;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ICommentServices {
    ResponseEntity<Object> createComment(CreateCommentRequest request) throws BadRequestException, InternalServerException;

    ResponseEntity<Object> deleteComment(UUID comment_id) throws BadRequestException, InternalServerException;

    ResponseEntity<Object> updateComment(UpdateCommentRequest request) throws BadRequestException, InternalServerException;

    ResponseEntity<Object> list(UUID postId) throws BadRequestException, InternalServerException;
}
