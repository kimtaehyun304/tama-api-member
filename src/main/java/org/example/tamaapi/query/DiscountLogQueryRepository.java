package org.example.tamaapi.query;

import org.example.tamaapi.domain.DiscountLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountLogQueryRepository extends JpaRepository<DiscountLog, Long> {

    boolean existsByPaymentId(String paymentId);

    List<DiscountLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
