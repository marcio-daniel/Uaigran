package com.uaigran.models.services.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {
    public UUID comment_id;
    public String comment_text;
}
