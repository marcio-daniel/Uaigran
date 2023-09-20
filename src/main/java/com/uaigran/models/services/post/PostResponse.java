package com.uaigran.models.services.post;

import com.uaigran.models.services.comment.CommentResponse;
import com.uaigran.models.services.like.LikeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    public UUID post_id;
    public String description;
    public String photoUri;
    public Date post_date;
    public List<CommentResponse> comment_list;
    public List<LikeResponse> like_list;
}
