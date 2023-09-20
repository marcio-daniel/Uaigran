package com.uaigran.models.services.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    public String description;
    public MultipartFile photo;
    public UUID post_owner;
}
