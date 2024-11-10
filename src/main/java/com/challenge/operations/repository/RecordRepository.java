package com.challenge.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.challenge.operations.entity.Record;
import java.util.List;

/**
 * RecordRepository is an interface that extends JpaRepository to provide
 * CRUD operations for the Record entity.
 *
 * This repository includes additional functionality to find records by user ID.
 */
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByUserId(Long userId);
}
