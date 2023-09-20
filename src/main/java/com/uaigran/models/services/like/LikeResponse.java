package com.uaigran.models.services.like;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
public class LikeResponse {

    public UUID like_id;
    public UUID post_id;
    public UUID user_id;
}
