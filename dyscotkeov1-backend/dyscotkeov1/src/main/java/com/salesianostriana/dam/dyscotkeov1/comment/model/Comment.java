package com.salesianostriana.dam.dyscotkeov1.comment.model;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userWhoPost", foreignKey = @ForeignKey(name = "FK_COMMENT_USER"))
    private User userWhoPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentedPost", foreignKey = @ForeignKey(name = "FK_COMMENT_POST"))
    private Post commentedPost;

    private LocalDate publishedDate;

    @OneToMany
    private List<User> usersWhoLiked;

}
