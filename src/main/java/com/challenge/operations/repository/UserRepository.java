package com.challenge.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.challenge.operations.entity.User;

import java.util.Optional;

/**
 * UserRepository interface for accessing and manipulating User entities in the database.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods.
 *
 * @param <User> The entity type that the repository manages.
 * @param <Long> The type of the entity's identifier.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
