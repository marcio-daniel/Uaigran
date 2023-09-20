package com.uaigran.controllers;

import com.uaigran.docs.swagger.PostControllerDocs;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.FileProcessingException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.post.CreatePostRequest;
import com.uaigran.models.services.post.IPostServices;
import com.uaigran.models.services.post.UpdatePostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController implements PostControllerDocs {

    @Autowired
    private IPostServices _postServices;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@ModelAttribute CreatePostRequest request) throws InternalServerException, BadRequestException, FileProcessingException {
        return _postServices.createPost(request);
    }

    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<Object> delete(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException {
        return _postServices.deletePost(post_id);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> update(@RequestBody UpdatePostRequest request) throws InternalServerException, BadRequestException {
        return _postServices.updatePost(request);
    }

    @GetMapping("/list/{user_id}")
    public ResponseEntity<Object> list(@PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException {
        return _postServices.list(user_id);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<Object> getPost(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException {
        return _postServices.getPost(post_id);
    }

    @GetMapping("/feed")
    public ResponseEntity<Object> feed() throws InternalServerException {
        return _postServices.feed();
    }
}
