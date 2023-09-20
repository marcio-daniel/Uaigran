package com.uaigran.models.services.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {
    public UUID post_id;
    public String description;
    public String photoUri;
}
