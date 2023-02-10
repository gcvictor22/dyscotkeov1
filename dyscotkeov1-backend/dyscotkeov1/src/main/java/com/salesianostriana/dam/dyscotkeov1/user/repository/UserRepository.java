package com.salesianostriana.dam.dyscotkeov1.user.repository;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    boolean existsByUserName(String s);

    @Query("""
            select u from User u
            left join fetch u.publishedPosts
            where u.id = :id
            """)
    Optional<User> userWithPosts (UUID id);

    @Query("""
            select u from User u
            left join fetch u.publishedPosts
            where u.userName = :userName
            """)
    Optional<User> userWithPosts (String userName);


    @Query("""
            select u from User u
            left join fetch u.publishedPosts
            """)
    List<User> userPosts();

    @EntityGraph("user-with-posts")
    Optional<User> findDistinctByUserName(String userName);
}
