package com.uaigran.models.services.comment;

import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.entities.Comment;
import com.uaigran.models.repository.ICommentRepository;
import com.uaigran.models.services.post.IPostServices;
import com.uaigran.models.services.user.IUserServices;
import com.uaigran.models.services.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommentServices implements ICommentServices {

    @Autowired
    private ICommentRepository _commentRepository;

    @Autowired
    private IUserServices _userServices;

    @Autowired
    private IPostServices _postServices;

    @Override
    public ResponseEntity<Object> createComment(CreateCommentRequest request) throws BadRequestException, InternalServerException {
        try{
            var user = _userServices.getUserById(request.user_id);
            var post = _postServices.getPostById(request.post_id);

            if( user == null){
                throw new BadRequestException("No user with these credentials was found!");
            }

            if( post== null){
                throw new BadRequestException("No post with these credentials was found!");
            }
            var comment = Comment
                    .builder()
                    .id(UUID.randomUUID())
                    .user(user)
                    .post(post)
                    .comment_text(request.comment_text)
                    .build();

            _commentRepository.save(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body("New comment created successfully!");
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deleteComment(UUID comment_id) throws BadRequestException, InternalServerException {
        try{
            if (_commentRepository.findById(comment_id).isEmpty()){
                throw new BadRequestException("No comment with these credentials was found!");
            }
            var comment = _commentRepository.findById(comment_id).get();
            _commentRepository.delete(comment);
            return ResponseEntity.ok().body("Comment deleted successfully!");
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updateComment(UpdateCommentRequest request) throws BadRequestException, InternalServerException {
        try {
            if (_commentRepository.findById(request.comment_id).isEmpty()){
                throw new BadRequestException("No comment with these credentials was found!");
            }
            var comment = _commentRepository.findById(request.comment_id).get();
            comment.setComment_text(request.comment_text);
            _commentRepository.save(comment);
            return ResponseEntity.ok().body("Comment successfully updated!");
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> list(UUID postId) throws BadRequestException, InternalServerException {
        try{
            var post = _postServices.getPostById(postId);
            if(post==null){
                throw new BadRequestException("No post with these credentials was found!");
            }
            var response = this.getCommentResponse(post.getComments());
            return ResponseEntity.ok().body(response);
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    private List<CommentResponse> getCommentResponse(List<Comment> comments) {
        var comment_list = new ArrayList<CommentResponse>();
        for (Comment c : comments){
            comment_list.add(
                    CommentResponse
                            .builder()
                            .comment_id(c.getId())
                            .comment_text(c.getComment_text())
                            .user(
                                    UserResponse
                                            .builder()
                                            .photoUri(c.getUser().getPhotoUri())
                                            .name(c.getUser().getName())
                                            .description(c.getUser().getDescription())
                                            .id(c.getUser().getId())
                                            .build()
                            )
                            .post_id(c.getPost().getPost_id())
                            .build()
            );
        }
        return comment_list;
    }
}
