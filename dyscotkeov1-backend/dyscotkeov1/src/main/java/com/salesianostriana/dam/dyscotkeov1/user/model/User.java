package com.salesianostriana.dam.dyscotkeov1.user.model;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
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
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String fullName;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;
    private String imgPath;
    private String description;
    private LocalDate joinDate;

    @ElementCollection
    private List<User> followers = new ArrayList<>();

    @ElementCollection
    private List<User> follow = new ArrayList<>();

    @OneToMany(mappedBy = "userWhoPost", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Post> publishedPosts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name="FK_LIKEDPOSTS_USERS")),
            inverseJoinColumns = @JoinColumn(name = "post_id",
                    foreignKey = @ForeignKey(name="FK_LIKEDPOSTS_POSTS")),
            name = "likedposts"
    )
    private List<Post> likedPosts = new ArrayList<>();

    private boolean verified;
}
