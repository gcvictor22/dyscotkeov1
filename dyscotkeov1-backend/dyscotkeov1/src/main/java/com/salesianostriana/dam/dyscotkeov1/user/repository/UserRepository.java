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

    boolean existsByEmail(String e);

    boolean existsByPhoneNumber(String p);

    @Query("""
            select u from User u
            left join fetch u.publishedPosts
            where u.id = :id
            """)
    Optional<User> userWithPostsById (UUID id);

    @Query("""
            select u from User u
            left join fetch u.publishedPosts
            where u.userName = :userName
            """)
    Optional<User> userWithPostsByUserName (String userName);

    @EntityGraph(value = "user-with-posts", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findDistinctByUserName(String userName);
}
