package org.tama.tamaapi.query;

import org.tama.tamaapi.domain.user.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CouponQueryRepository extends JpaRepository<Coupon, Long> {


}
