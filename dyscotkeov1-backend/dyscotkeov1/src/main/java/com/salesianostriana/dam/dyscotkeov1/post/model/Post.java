package com.salesianostriana.dam.dyscotkeov1.post.model;

import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.client.model.Client;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String affair;
    private String content;
    private String imgPath;

    @ManyToOne
    @JoinColumn(name = "clientWhoPost", foreignKey = @ForeignKey(name = "FK_CLIENT_COMMENT"))
    private Client clientWhoPost;

    @ManyToMany(mappedBy = "likedPosts", fetch = FetchType.LAZY)
    private List<Client> clientsWhoLiked;

    @OneToMany(mappedBy = "commentedPost")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    private LocalDate postDate;

}
