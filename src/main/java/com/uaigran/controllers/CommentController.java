package com.uaigran.controllers;

import com.uaigran.docs.swagger.CommentControllerDocs;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.comment.CreateCommentRequest;
import com.uaigran.models.services.comment.ICommentServices;
import com.uaigran.models.services.comment.UpdateCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController implements CommentControllerDocs {

    @Autowired
    private ICommentServices _commentServices;

    @PostMapping("/create")
    @Override
    public ResponseEntity<Object> createComment(@ModelAttribute CreateCommentRequest request) throws InternalServerException, BadRequestException {
        return  _commentServices.createComment(request);
    }

    @PostMapping("/update")
    @Override
    public ResponseEntity<Object> updateComment(@RequestBody UpdateCommentRequest request) throws InternalServerException, BadRequestException {
        return _commentServices.updateComment(request);
    }

    @GetMapping("/list/{post_id}")
    @Override
    public ResponseEntity<Object> list(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException {
        return _commentServices.list(post_id);
    }

    @DeleteMapping("/delete/{comment_id}")
    @Override
    public ResponseEntity<Object> deleteComment(@PathVariable("comment_id") UUID comment_id) throws InternalServerException, BadRequestException {
        return _commentServices.deleteComment(comment_id);
    }
}
