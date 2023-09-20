package com.uaigran.models.services.post;

import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.FileProcessingException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.entities.Post;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IPostServices {

    public ResponseEntity<Object> createPost(CreatePostRequest request) throws BadRequestException, InternalServerException, FileProcessingException;
    public ResponseEntity<Object> deletePost(UUID post_id) throws BadRequestException, InternalServerException;
    public ResponseEntity<Object> updatePost(UpdatePostRequest request) throws BadRequestException, InternalServerException;
    public ResponseEntity<Object> list(UUID user_id) throws BadRequestException, InternalServerException;
    public  ResponseEntity<Object> feed() throws InternalServerException;
    public Post getPostById(UUID post_id);

    public ResponseEntity<Object> getPost(UUID postId) throws BadRequestException, InternalServerException;
}
