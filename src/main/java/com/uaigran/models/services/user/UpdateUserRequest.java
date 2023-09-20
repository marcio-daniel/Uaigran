package com.uaigran.models.services.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    public UUID id;
    public String name;
    public MultipartFile photo;
    public String description;
}
