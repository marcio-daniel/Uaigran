package com.uaigran.models.services.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {

    public UUID user_id;
    public UUID post_id;
    public String comment_text;
}
