package com.salesianostriana.dam.dyscotkeov1.user.model;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String fullName;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;
    private String imgPath;
    private String description;
    private LocalDate joinDate;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> publishedPosts;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> likedPosts;

    @ManyToOne
    private List<User> followers;

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> follows;

    private boolean verified;
}
