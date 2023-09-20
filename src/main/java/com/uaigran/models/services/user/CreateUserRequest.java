package com.uaigran.models.services.user;

import com.uaigran.models.entities.User.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    public String name;
    public String email;
    public String password;
    public UserRoles role;
}
