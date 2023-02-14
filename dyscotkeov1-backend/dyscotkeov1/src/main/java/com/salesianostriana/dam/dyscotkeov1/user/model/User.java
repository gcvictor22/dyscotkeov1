package com.salesianostriana.dam.dyscotkeov1.user.model;

import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user_entity")
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph
        (name="user-with-posts",
                attributeNodes = {
                        @NamedAttributeNode(value = "publishedPosts")
                })
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String fullName;
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;
    private String imgPath;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_who_is_followed_id",
            foreignKey = @ForeignKey(name="FK_FOLLOWS_USER")),
            inverseJoinColumns = @JoinColumn(name = "user_who_follows_id",
                    foreignKey = @ForeignKey(name="FK_FOLLOWERS_USER")),
            name = "userfollows"
    )
    @Builder.Default
    private List<User> followers = new ArrayList<>();

    @ManyToMany(mappedBy = "followers", fetch = FetchType.EAGER)
    @Builder.Default
    private List<User> follows = new ArrayList<>();

    @OneToMany(mappedBy = "userWhoPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Post> publishedPosts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name="FK_LIKEDPOSTS_USER")),
            inverseJoinColumns = @JoinColumn(name = "post_id",
                    foreignKey = @ForeignKey(name="FK_LIKEDPOSTS_POSTS")),
            name = "likedposts"
    )
    private List<Post> likedPosts = new ArrayList<>();

    private boolean verified;

    @Builder.Default
    private boolean accountNonExpired = true;
    @Builder.Default
    private boolean accountNonLocked = true;
    @Builder.Default
    private boolean credentialsNonExpired = true;
    @Builder.Default
    private boolean enabled = true;

    @Convert(converter = EnumRoleConverter.class)
    private EnumSet<UserRole> roles;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder.Default
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    /******************************/
    /*           HELPERS           /
    /******************************/

    public void giveAFollow(User loggedUser, boolean b){
        List<User> aux1 = this.getFollowers();
        List<User> aux2 = loggedUser.getFollows();
        if (!b && aux1.size() > 0 && aux2.size() > 0){
            aux1.remove(this.getFollowers().indexOf(loggedUser)+1);
            aux2.remove(loggedUser.getFollows().indexOf(this)+1);
        }else {
            aux1.add(loggedUser);
            aux2.add(this);
            if (aux1.size()>=10){
                this.setVerified(true);
            }
        }
        this.setFollowers(aux1);
        loggedUser.setFollows(aux2);
    }
}
