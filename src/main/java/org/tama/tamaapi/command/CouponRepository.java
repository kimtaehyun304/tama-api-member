package org.tama.tamaapi.command;

import org.tama.tamaapi.domain.user.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {


}
