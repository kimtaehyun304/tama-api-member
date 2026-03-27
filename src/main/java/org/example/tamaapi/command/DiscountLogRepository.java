package org.example.tamaapi.command;

import org.example.tamaapi.domain.DiscountLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountLogRepository extends JpaRepository<DiscountLog, Long> {

    boolean existsByPaymentId(String paymentId);
}
