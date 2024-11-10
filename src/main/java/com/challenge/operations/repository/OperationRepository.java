package com.challenge.operations.repository;

import com.challenge.operations.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<Operation> findByType(String type);
}

