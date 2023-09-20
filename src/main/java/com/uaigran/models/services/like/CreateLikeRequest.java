package com.uaigran.models.services.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLikeRequest {
    public UUID user_id;
    public UUID post_id;
}
