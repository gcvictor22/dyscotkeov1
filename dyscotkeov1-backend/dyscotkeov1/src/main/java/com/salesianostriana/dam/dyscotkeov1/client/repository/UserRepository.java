package com.salesianostriana.dam.dyscotkeov1.client.repository;

import com.salesianostriana.dam.dyscotkeov1.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Client, Long> {
}
