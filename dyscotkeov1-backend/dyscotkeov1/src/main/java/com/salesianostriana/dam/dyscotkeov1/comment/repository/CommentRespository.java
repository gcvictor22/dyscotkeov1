package com.salesianostriana.dam.dyscotkeov1.comment.repository;

import com.salesianostriana.dam.dyscotkeov1.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRespository extends JpaRepository<Comment, Long> {
}
