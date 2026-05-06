package org.tama.tamaapi.query;

import lombok.RequiredArgsConstructor;
import org.tama.tamaapi.domain.user.coupon.CouponType;
import org.tama.tamaapi.domain.user.coupon.MemberCoupon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.tama.tamaapi.exception.ErrorMessageUtil.NOT_FOUND_COUPON;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponQueryService {

    private final MemberCouponQueryRepository memberCouponQueryRepository;

    public int getCouponPrice(Long memberCouponId, int orderItemsPrice) {
        if (memberCouponId == null) return 0;

        MemberCoupon memberCoupon = memberCouponQueryRepository.findWithById(memberCouponId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_COUPON));

        CouponType couponType = memberCoupon.getCoupon().getType();
        int discountValue = memberCoupon.getCoupon().getDiscountValue();

        int couponPrice = switch (couponType) {
            case FIXED_DISCOUNT -> discountValue;
            case PERCENT_DISCOUNT -> (int) Math.round(orderItemsPrice * (discountValue / 100.0));
        };

        //validateCoupon은 조회말고 저장할 때

        return couponPrice;
    }


}
