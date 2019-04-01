package com.test.demo.repository;

import com.test.demo.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<EventLog, String> {
    EventLog findByDbId(String dbId);

    EventLog getById(String id);
}
