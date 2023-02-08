package com.salesianostriana.dam.dyscotkeov1.comment.model;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userWhoPost;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post commentedPost;

    private LocalDate publishedDate;

    @OneToMany
    private List<User> usersWhoLiked;

}
