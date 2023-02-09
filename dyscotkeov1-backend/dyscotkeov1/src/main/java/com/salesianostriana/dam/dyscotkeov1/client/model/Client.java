package com.salesianostriana.dam.dyscotkeov1.client.model;

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
public class Client implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String fullName;
    private String userName;
    private String phoneNumber;
    private String email;
    private String imgPath;
    private String description;
    private LocalDate joinDate;

    @ElementCollection
    private List<Client> followers = new ArrayList<>();

    @ElementCollection
    private List<Client> follow = new ArrayList<>();

    @OneToMany(mappedBy = "clientWhoPost", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Post> publishedPosts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "client_id",
            foreignKey = @ForeignKey(name="FK_LIKEDPOSTS_CLIENT")),
            inverseJoinColumns = @JoinColumn(name = "post_id",
                    foreignKey = @ForeignKey(name="FK_LIKEDPOSTS_POSTS")),
            name = "likedposts"
    )
    private List<Post> likedPosts = new ArrayList<>();

    private boolean verified;
}
