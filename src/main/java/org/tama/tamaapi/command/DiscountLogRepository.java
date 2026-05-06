package org.tama.tamaapi.command;

import org.tama.tamaapi.domain.DiscountLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountLogRepository extends JpaRepository<DiscountLog, Long> {

    boolean existsByPaymentId(String paymentId);
}
