package com.Marc.Test.repository;

import com.Marc.Test.repository.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, String> {
}
