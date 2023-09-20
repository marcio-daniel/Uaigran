package com.uaigran.models.services.comment;

import com.uaigran.models.services.user.UserResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public class CommentResponse {
    public UUID comment_id;
    public String comment_text;
    public UserResponse user;
    public UUID post_id;
}
