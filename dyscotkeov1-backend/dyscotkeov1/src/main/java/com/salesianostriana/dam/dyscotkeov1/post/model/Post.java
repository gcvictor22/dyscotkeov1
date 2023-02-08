package com.salesianostriana.dam.dyscotkeov1.post.model;

import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private Long id;
    private String affair;
    private String content;
    private String imgPath;

    @ManyToOne
    private User userWhoPost;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> usersWhoLiked;
    private LocalDate postDate;

}
