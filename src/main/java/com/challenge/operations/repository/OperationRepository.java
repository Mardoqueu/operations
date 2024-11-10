package com.challenge.operations.repository;

import com.challenge.operations.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link Operation} entities.
 *
 * This interface extends the JpaRepository interface, which provides various methods
 * for interacting with the database, such as saving, finding, and deleting entities.
 */
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<Operation> findByType(String type);
}

