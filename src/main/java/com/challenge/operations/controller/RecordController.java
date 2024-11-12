package com.challenge.operations.controller;

import com.challenge.operations.entity.Record;
import com.challenge.operations.entity.User;
import com.challenge.operations.service.RecordService;
import com.challenge.operations.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Record>> getUserRecords() {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
            User authenticatedUser = userService.getAuthenticatedUser();
            if (authenticatedUser == null) {
                logger.warn("Unauthorized access attempt.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<com.challenge.operations.entity.Record> records = recordService.listRecordsByUser(authenticatedUser.getId());
            if (records == null || records.isEmpty()) {
                logger.info("No records found for user id {}", authenticatedUser.getId());
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(records);
        } catch (Exception e) {
            logger.error("Error retrieving records", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        User authenticatedUser = userService.getAuthenticatedUser();

        recordService.deleteRecordById(id, authenticatedUser.getId());

        return ResponseEntity.noContent().build();
    }
}

