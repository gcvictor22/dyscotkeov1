package com.salesianostriana.dam.dyscotkeov1.user.repository;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
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


    @Query("""
            SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
            FROM User u
            JOIN u.followers f
            WHERE u.id = :id1
            AND f.id = :id2
            """)
    boolean checkFollow(@Param("id1") UUID id1, @Param("id2") UUID id2);
}
