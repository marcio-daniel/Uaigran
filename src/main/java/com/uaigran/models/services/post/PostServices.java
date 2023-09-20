package com.uaigran.models.services.post;

import com.uaigran.exceptions.FileProcessingException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.models.entities.Comment;
import com.uaigran.models.entities.Like;
import com.uaigran.models.entities.Post;
import com.uaigran.models.repository.IPostRepository;
import com.uaigran.models.services.comment.CommentResponse;
import com.uaigran.models.services.fileUpload.IFileUploadService;
import com.uaigran.models.services.like.LikeResponse;
import com.uaigran.models.services.user.IUserServices;
import com.uaigran.models.services.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostServices implements IPostServices{

    @Autowired
    private IPostRepository _postRepository;

    @Autowired
    private IUserServices _userServices;

    @Autowired
    private IFileUploadService _fileUploadServices;
    @Override
    public ResponseEntity<Object> createPost(CreatePostRequest request) throws BadRequestException, InternalServerException, FileProcessingException {
        try{
            var user_owner = _userServices.getUserById(request.post_owner);
            if(user_owner == null){
                throw new BadRequestException("No user with these credentials was found!");
            }
            var photoUri = "";
            if(request.photo != null){
                try {
                    var fileName = UUID.randomUUID() + "." + request.photo
                            .getOriginalFilename()
                            .substring(
                                    request.photo.getOriginalFilename()
                                            .lastIndexOf(".") + 1);

                    photoUri = _fileUploadServices.upload(request.photo, fileName);

                } catch (Exception e) {
                    throw  new FileProcessingException("There was an error processing the image!");
                }
            }

            var newPost = Post
                    .builder()
                    .post_date(new Date())
                    .post_id(UUID.randomUUID())
                    .photoUri(photoUri)
                    .description(request.description)
                    .user(user_owner)
                    .build();

            _postRepository.save(newPost);
            return ResponseEntity.status(HttpStatus.CREATED).body("The new post was created successfully!");

        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (FileProcessingException e){
            throw new FileProcessingException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deletePost(UUID post_id) throws BadRequestException, InternalServerException {
        try {
            var post = _postRepository.findById(post_id);
            if(post.isEmpty()){
                throw new BadRequestException("No post with these credentials was found!");
            }
            _postRepository.delete(post.get());
            return ResponseEntity.ok().body("Post successfully deleted!");
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updatePost(UpdatePostRequest request) throws BadRequestException, InternalServerException {
        try {
            if (_postRepository.findById(request.post_id).isEmpty()){
                throw new BadRequestException("No post with these credentials was found!");
            }
            var post = _postRepository.findById(request.post_id).get();
            post.setDescription(request.description);
            post.setPhotoUri(request.photoUri);
            _postRepository.save(post);
            return ResponseEntity.ok().body("Post successfully updated!");

        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> list(UUID user_id) throws BadRequestException, InternalServerException {

        try {
            var user = _userServices.getUserById(user_id);
            if( user == null){
                throw new BadRequestException("No user with these credentials was found!");
            }
            var post_list = _postRepository.findByUser(user);
            List<PostResponse> response = new ArrayList<>();
            for (Post p : post_list){
                response.add(
                        PostResponse
                                .builder()
                                .post_id(p.getPost_id())
                                .description(p.getDescription())
                                .post_date(p.getPost_date())
                                .photoUri(p.getPhotoUri())
                                .comment_list(this.getCommentResponse(p.getComments()))
                                .like_list(this.getLikeResponse(p.getLikes()))
                                .build());

            }
            return ResponseEntity.ok().body(response);
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> feed() throws InternalServerException {
        try {
            var post_list = _postRepository.findAll();
            List<PostResponse> response = new ArrayList<>();
            for (Post p : post_list){
                response.add(
                        PostResponse
                                .builder()
                                .post_id(p.getPost_id())
                                .description(p.getDescription())
                                .post_date(p.getPost_date())
                                .photoUri(p.getPhotoUri())
                                .comment_list(this.getCommentResponse(p.getComments()))
                                .like_list(this.getLikeResponse(p.getLikes()))
                                .build());

            }
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    private List<LikeResponse> getLikeResponse(List<Like> likes) {
        var like_list = new ArrayList<LikeResponse>();
        for (Like l : likes){
            like_list.add(
                    LikeResponse
                            .builder()
                            .like_id(l.getId())
                            .post_id(l.getPost().getPost_id())
                            .user_id(l.getUser().getId())
                            .build()
            );
        }
        return like_list;
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

    @Override
    public Post getPostById(UUID post_id) {
        try {
            var post = _postRepository.findById(post_id).get();
            return post;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public ResponseEntity<Object> getPost(UUID postId) throws BadRequestException, InternalServerException {
        try{
            if(_postRepository.findById(postId).isEmpty()){
                throw new BadRequestException("No post with these credentials was found!");
            }
            var post = _postRepository.findById(postId).get();
            var response = PostResponse
                    .builder()
                    .post_date(post.getPost_date())
                    .post_id(post.getPost_id())
                    .description(post.getDescription())
                    .comment_list(this.getCommentResponse(post.getComments()))
                    .like_list(this.getLikeResponse(post.getLikes()))
                    .photoUri(post.getPhotoUri())
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

}
