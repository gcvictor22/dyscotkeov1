package com.salesianostriana.dam.dyscotkeov1.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDto {

    private String userName;
    private String fullName;
    private String imgPath;
    private List<GetUserDto> follows;
    private List<GetUserDto> followers;
    private List<Post> publishedPosts;
    private List<Post> likedPosts;
    private boolean verified;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;

    public static UserProfileDto of(User user){
        return UserProfileDto.builder()
                .userName(user.getUsername())
                .fullName(user.getFullName())
                .imgPath(user.getImgPath())
                .createdAt(user.getCreatedAt())
                .follows(user.getFollows().stream().map(GetUserDto::of).collect(Collectors.toList()))
                .followers(user.getFollowers().stream().map(GetUserDto::of).collect(Collectors.toList()))
                .publishedPosts(user.getPublishedPosts())
                .likedPosts(user.getLikedPosts())
                .verified(user.isVerified())
                .build();
    }

}
