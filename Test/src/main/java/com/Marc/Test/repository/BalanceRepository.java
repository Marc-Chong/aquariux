package com.Marc.Test.repository;

import com.Marc.Test.repository.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, String> {
}
