package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserResponse extends GetUserDto{

    private String token;
    private String refreshToken;

    public JwtUserResponse(GetUserDto user) {
        UUID id = user.getId();
        String username = user.getUserName();
        String fullName = user.getFullName();
        String avatar = user.getImgPath();
        LocalDateTime createdAt = user.getCreatedAt();
    }

    public static JwtUserResponse of (User user, String token, String refreshToken) {
        JwtUserResponse result = new JwtUserResponse(GetUserDto.of(user));
        result.setToken(token);
        result.setRefreshToken(refreshToken);
        return result;

    }

}
