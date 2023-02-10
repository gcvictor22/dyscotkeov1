package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetUserDto {

    private UUID id;
    private String userName;
    private String fullName;
    private String imgPath;
    private int followers;
    private int countOfPosts;
    private LocalDateTime createdAt;

    public static GetUserDto of(User c){
        return GetUserDto.builder()
                .id(c.getId())
                .userName(c.getUsername())
                .fullName(c.getFullName())
                .imgPath(c.getImgPath())
                .followers(c.getFollowers() == null ? 0 : c.getFollowers().size())
                .countOfPosts(c.getPublishedPosts() == null ? 0 : c.getPublishedPosts().size())
                .createdAt(c.getCreatedAt())
                .build();
    }

}
