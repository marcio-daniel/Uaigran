package com.uaigran.models.services.user;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class UserResponse {
    public UUID id;
    public String name;
    public String description;
    public String photoUri;
}
