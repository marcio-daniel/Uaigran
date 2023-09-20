package com.uaigran.controllers;

import com.uaigran.docs.swagger.LikeControllerDocs;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.like.CreateLikeRequest;
import com.uaigran.models.services.like.ILikeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/like")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LikeController implements LikeControllerDocs {

    @Autowired
    private ILikeServices _likeServices;

    @PostMapping("/create")
    public ResponseEntity<Object> createLike(@RequestBody CreateLikeRequest request) throws InternalServerException, BadRequestException {
        return _likeServices.createLike(request);
    }

    @GetMapping("/{post_id}/{user_id}")
    public ResponseEntity<Object> getLike(@PathVariable("post_id") UUID post_id,@PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException {
        return _likeServices.getLike(post_id,user_id);
    }

    @DeleteMapping("/delete/{like_id}")
    public ResponseEntity<Object> deleteLike(@PathVariable("like_id") UUID like_id) throws InternalServerException, BadRequestException {
        return _likeServices.deleteLike(like_id);
    }
}
