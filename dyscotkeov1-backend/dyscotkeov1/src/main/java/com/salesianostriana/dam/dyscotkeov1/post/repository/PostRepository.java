package com.salesianostriana.dam.dyscotkeov1.post.repository;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
