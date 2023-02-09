package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetUserDto {

    private String userName;
    private String fullName;
    private String imgPath;
    private int followers;
    private int countOfPosts;

    public static GetUserDto of(User c){
        return GetUserDto.builder()
                .userName(c.getUsername())
                .fullName(c.getFullName())
                .imgPath(c.getImgPath())
                .followers(c.getFollowers().size())
                .countOfPosts(c.getPublishedPosts().size())
                .build();
    }

}
