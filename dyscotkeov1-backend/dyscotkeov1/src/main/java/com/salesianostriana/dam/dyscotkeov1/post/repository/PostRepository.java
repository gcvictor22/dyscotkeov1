package com.salesianostriana.dam.dyscotkeov1.post.repository;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("""
            select distinct p from Post p
            left join fetch p.comments
            """)
    List<Post> postComments();

    @Query("""
            SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
            FROM Post p JOIN p.usersWhoLiked l WHERE p.id = :postId AND l.id = :userId""")
    boolean existsLikeByUser(@Param("postId") Long postId, @Param("userId") UUID userId);
}
