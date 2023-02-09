package com.salesianostriana.dam.dyscotkeov1.user.repository;

import com.salesianostriana.dam.dyscotkeov1.post.model.Post;
import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    boolean existsByUserName(String s);

    @EntityGraph("user-with-posts")
    Optional<User> findDistinctByUserName(String nombre);

    @EntityGraph("user-with-posts")
    Optional<User> findById(UUID id);
}
