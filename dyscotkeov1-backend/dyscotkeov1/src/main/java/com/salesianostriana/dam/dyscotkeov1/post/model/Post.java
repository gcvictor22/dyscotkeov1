package com.salesianostriana.dam.dyscotkeov1.post.model;

import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String affair;
    private String content;
    private String imgPath;

    @ManyToOne
    @JoinColumn(name = "userWhoPost", foreignKey = @ForeignKey(name = "FK_USER_COMMENT"))
    private User userWhoPost;

    @ManyToMany(mappedBy = "likedPosts", fetch = FetchType.EAGER)
    private List<User> usersWhoLiked;

    @OneToMany(mappedBy = "commentedPost")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime postDate;

}
