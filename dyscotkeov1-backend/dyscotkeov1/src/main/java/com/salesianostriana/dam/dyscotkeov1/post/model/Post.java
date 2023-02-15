package com.salesianostriana.dam.dyscotkeov1.post.model;

import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
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

    @OneToMany(mappedBy = "commentedPost", orphanRemoval = true, cascade = CascadeType.ALL)
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

    @PreRemove
    public void preRemove(){
        if (this.userWhoPost != null)
            this.userWhoPost.getPublishedPosts().remove(this);
        this.getUsersWhoLiked().forEach(u -> {
            u.getLikedPosts().remove(this);
        });

        this.getUsersWhoLiked().clear();
    }

    public void like(User user, boolean b){

        List<Post> aux1 = user.getLikedPosts();
        List<User> aux2 = this.getUsersWhoLiked();
        if (!b){
            aux1.add(this);
            aux2.add(user);
        }else {
            aux1.remove(user.getLikedPosts().indexOf(this)+1);
            aux2.remove(this.getUsersWhoLiked().indexOf(user)+1);
        }
        this.setUsersWhoLiked(aux2);
        user.setLikedPosts(aux1);
    }

}
