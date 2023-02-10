package com.salesianostriana.dam.dyscotkeov1.post.repository;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            select distinct p from Post p
            left join fetch p.comments
            """)
    List<Post> postComments();
}
