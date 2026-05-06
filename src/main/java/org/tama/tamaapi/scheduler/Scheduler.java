package org.tama.tamaapi.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.tama.tamaapi.command.CouponService;
import org.tama.tamaapi.domain.DiscountLog;
import org.tama.tamaapi.domain.user.Authority;
import org.tama.tamaapi.domain.user.Member;
import org.tama.tamaapi.domain.user.coupon.Coupon;
import org.tama.tamaapi.domain.user.coupon.MemberCoupon;
import org.tama.tamaapi.feignClient.order.OrderFeignClient;
import org.tama.tamaapi.query.DiscountLogQueryRepository;
import org.tama.tamaapi.query.MemberCouponQueryRepository;
import org.tama.tamaapi.query.MemberQueryRepository;

import org.tama.tamaapi.command.CouponRepository;
import org.tama.tamaapi.command.MemberCouponRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final MemberCouponQueryRepository memberCouponQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final DiscountLogQueryRepository discountLogQueryRepository;
    private final OrderFeignClient orderFeignClient;
    private final CouponService couponService;

    //역할
    //1. 쿠폰 사용됐는데, 주문 저장전에 서버 down돼서 재고 롤백 안 된거 롤백
    //2. 주문 완료된 쿠폰 로그 삭제 (의도한대로 및 정상적으로 끝난 케이스)

    //1시간 주기가 적당한듯 (장애는 흔하지 않으니까)
    //fixedDelay는 앱 시작시에 바로 실행
    @Scheduled(fixedDelay = 1000*60*60, zone = "Asia/Seoul")
    public void checkAndRollbackStock(){
        //3시간동안 상품 서버 down 될 가능성 고려 (더 길게 장애나는건 수동으로 처리)
        //토스뱅크는 지연되면 알람오게 하더라
        LocalDateTime start = LocalDateTime.now().minusHours(3);
        //최근 주문은 아직 진행 중이라, 재고 차감 단계 까지만 진행된 걸 수 있어서 제외
        LocalDateTime end = LocalDateTime.now().minusMinutes(10);

        List<DiscountLog> logs = discountLogQueryRepository.findByCreatedAtBetween(start, end);
        List<String> paymentIds = logs.stream().map(DiscountLog::getPaymentId).toList();
        Set<String> orderedPaymentIds = new HashSet<>(orderFeignClient.findExistingPaymentIds(paymentIds));
        Set<String> orderedSet = new HashSet<>(orderedPaymentIds);

        //원래 삭제해야할 로그
        List<DiscountLog> orderLogs = new ArrayList<>();

        //재고 롤백후 삭제할 로그
        List<DiscountLog> deleteLogs = new ArrayList<>();

        for (DiscountLog log : logs) {
            if (orderedSet.contains(log.getPaymentId())) orderLogs.add(log);
            else deleteLogs.add(log);
        }

        //주문이 완료된 로그 삭제 (정상적으로 끝난 케이스)
        List<String> orderLogPaymentIds = orderLogs.stream().map(DiscountLog::getPaymentId).toList();
        couponService.deleteDiscountLogInPaymentIds(orderLogPaymentIds);

        //쿠폰은 사용했지만 주문 저장 전에, 주문 서버 down 돼서 쿠폰 롤백시키는 케이스
        for (DiscountLog deleteLog : deleteLogs) {
            Long memberCouponId = deleteLog.getMemberCouponId();
            Integer usedPoint = deleteLog.getUsedPoint();
            Integer rewardPoint = deleteLog.getRewardPoint();
            Long memberId = deleteLog.getMemberId();
            String paymentId = deleteLog.getPaymentId();
            couponService.rollbackDiscountAndDeleteLog(memberCouponId, usedPoint, rewardPoint, memberId, paymentId);
        }
    }

    //@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    //체험용 계정에 쿠폰 발급 (다 썼을 경우)
    //체험용 계정은 면접관이 기능 체험할때 쓰라고 만든 계정
    public void giveCoupon() {
        Member experienceAccount = memberQueryRepository.findAllByAuthority(Authority.MEMBER).get(1);
        boolean isAllCouponsUsed = !memberCouponQueryRepository.existsByMemberIdAndIsUsedIsFalse(experienceAccount.getId());

        if(isAllCouponsUsed){
            List<Coupon> coupons = couponRepository.findAllById(List.of(1L, 2L, 3L, 4L, 5L, 6L));
            memberCouponRepository.save(new MemberCoupon(coupons.get(0), experienceAccount, false));
            memberCouponRepository.save(new MemberCoupon(coupons.get(1), experienceAccount, false));
            memberCouponRepository.save(new MemberCoupon(coupons.get(2), experienceAccount, false));

            memberCouponRepository.save(new MemberCoupon(coupons.get(3), experienceAccount, false));
            memberCouponRepository.save(new MemberCoupon(coupons.get(4), experienceAccount, false));
            memberCouponRepository.save(new MemberCoupon(coupons.get(5), experienceAccount, false));
        }
    }



}
