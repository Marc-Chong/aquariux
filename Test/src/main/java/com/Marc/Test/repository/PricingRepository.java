package com.Marc.Test.repository;

import com.Marc.Test.repository.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingRepository extends JpaRepository<Pricing, String> {
}
