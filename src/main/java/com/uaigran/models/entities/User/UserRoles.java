package com.uaigran.models.entities.User;

public enum UserRoles {

    USER("user");

    private String role;

    UserRoles(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
