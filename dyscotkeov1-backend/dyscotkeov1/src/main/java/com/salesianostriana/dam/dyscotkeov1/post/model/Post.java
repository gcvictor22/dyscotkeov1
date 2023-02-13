package com.salesianostriana.dam.dyscotkeov1.post.model;

import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.parameters.P;

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
@EntityListeners(AuditingEntityListener.class)
public class Post implements Serializable{

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

    /******************************/
    /*           HELPERS           /
    /******************************/

    public void addUser(User user){
        this.setUserWhoPost(user);
        List<Post> aux = user.getPublishedPosts();
        aux.add(this);
    }

    public void removeUser(User user){
        List<Post> aux = user.getPublishedPosts();
        aux.remove(this);
    }

    public void like(User user){

        List<Post> aux1 = user.getLikedPosts();
        List<User> aux2 = this.getUsersWhoLiked();
        if (!user.getLikedPosts().contains(this) && !this.getUsersWhoLiked().contains(user)){
            aux1.add(this);
            aux2.add(user);
        }else {
            aux1.remove(this);
            aux2.remove(user);
        }
        this.setUsersWhoLiked(aux2);
        user.setLikedPosts(aux1);
    }

}
