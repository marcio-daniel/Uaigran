package com.uaigran.models.services.like;

import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.entities.Like;
import com.uaigran.models.repository.ILikeRepository;
import com.uaigran.models.services.post.IPostServices;
import com.uaigran.models.services.user.IUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LikeServices implements ILikeServices{

    @Autowired
    private IPostServices _postServices;

    @Autowired
    private IUserServices _userServices;

    @Autowired
    private ILikeRepository _likeRepository;

    @Override
    public ResponseEntity<Object> createLike(CreateLikeRequest request) throws BadRequestException, InternalServerException {
        try {
            var post = _postServices.getPostById(request.post_id);
            var user = _userServices.getUserById(request.user_id);

            if (post == null) {
                throw new BadRequestException("No post with these credentials was found!");
            }
            if (user == null) {
                throw new BadRequestException("No user with these credentials was found!");
            }
            var like = Like
                    .builder()
                    .id(UUID.randomUUID())
                    .user(user)
                    .post(post)
                    .build();
            _likeRepository.save(like);
            var response = LikeResponse
                    .builder()
                    .like_id(like.getId())
                    .user_id(like.getUser().getId())
                    .post_id(like.getPost().getPost_id())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deleteLike(UUID like_id) throws BadRequestException, InternalServerException {
        try {
            if (_likeRepository.findById(like_id).isEmpty()){
                throw new BadRequestException("No like with these credentials was found!");
            }
            var like = _likeRepository.findById(like_id).get();
            _likeRepository.delete(like);
            return ResponseEntity.status(HttpStatus.CREATED).body("Like successfully deleted!");
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getLike(UUID post_id,UUID user_id) throws BadRequestException, InternalServerException {
        try {
            var post = _postServices.getPostById(post_id);
            var user = _userServices.getUserById(user_id);
            if( post  == null){
                throw new BadRequestException("No post with these credentials was found!");
            }
            if(user == null){
                throw new BadRequestException("No user with these credentials was found!");
            }
            for (Like l : post.getLikes()){
                if(l.getUser().getId() == user.getId()){
                    var response = LikeResponse
                            .builder()
                            .like_id(l.getId())
                            .post_id(l.getPost().getPost_id())
                            .user_id(l.getUser().getId())
                            .build();
                    return ResponseEntity.ok().body(response);
                }
            }
            return ResponseEntity.ok().body(null);
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }
}
