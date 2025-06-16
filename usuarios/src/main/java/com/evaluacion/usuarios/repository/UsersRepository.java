package com.evaluacion.usuarios.repository;

import com.evaluacion.usuarios.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID>{
    Optional<User> findByEmail(String email);
}