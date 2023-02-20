package com.salesianostriana.dam.dyscotkeov1.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.dto.GetUserDto;
import com.salesianostriana.dam.dyscotkeov1.user.dto.UserWhoLikeDto;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import com.salesianostriana.dam.dyscotkeov1.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPostDto {

    private Long id;
    private String affair;
    private String content;
    private List<String> imgPath;
    private UserWhoLikeDto userWhoPost;
    private int usersWhoLiked;
    private int comments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime postDate;

    public static GetPostDto of(Post post){
        return GetPostDto.builder()
                .id(post.getId())
                .affair(post.getAffair())
                .content(post.getContent())
                .imgPath(post.getImgPaths())
                .userWhoPost(UserWhoLikeDto.of(post.getUserWhoPost()))
                .usersWhoLiked(post.getUsersWhoLiked() == null ? 0 : post.getUsersWhoLiked().size())
                .comments(post.getComments() == null ? 0 : post.getComments().size())
                .postDate(post.getPostDate())
                .build();
    }

}
