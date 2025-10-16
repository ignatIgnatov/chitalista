package com.community_centers.service.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UserResponse user;

    public JwtResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }
}
