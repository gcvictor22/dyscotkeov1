package com.salesianostriana.dam.dyscotkeov1.user.repository;

import com.salesianostriana.dam.dyscotkeov1.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
