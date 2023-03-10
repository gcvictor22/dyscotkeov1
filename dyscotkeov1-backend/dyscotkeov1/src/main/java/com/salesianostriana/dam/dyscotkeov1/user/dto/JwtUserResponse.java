package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class JwtUserResponse extends GetUserDto{

    private String token;

    public JwtUserResponse(GetUserDto user) {
        id = user.getId();
        userName = user.getUserName();
        fullName = user.getFullName();
        imgPath = user.getImgPath();
        createdAt = user.getCreatedAt();
        followers = user.getFollowers();
        countOfPosts = user.getCountOfPosts();
        verified = user.isVerified();
    }

    public static JwtUserResponse of (User user, String token) {
        JwtUserResponse result = new JwtUserResponse(GetUserDto.of(user));
        result.setToken(token);

        return result;
    }

}
