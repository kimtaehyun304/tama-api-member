package org.tama.tamaapi.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tama.tamaapi.config.cache.MyCacheType;
import org.tama.tamaapi.domain.user.Authority;
import org.tama.tamaapi.domain.user.Member;
import org.tama.tamaapi.domain.user.MemberAddress;
import org.tama.tamaapi.domain.user.Provider;
import org.tama.tamaapi.domain.user.coupon.Coupon;
import org.tama.tamaapi.domain.user.coupon.CouponType;
import org.tama.tamaapi.domain.user.coupon.MemberCoupon;
import org.tama.tamaapi.exception.ErrorMessageUtil;
import org.tama.tamaapi.dto.requestDto.member.SignUpMemberRequest;
import org.tama.tamaapi.query.MemberAddressQueryRepository;
import org.tama.tamaapi.query.MemberQueryRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static org.tama.tamaapi.exception.ErrorMessageUtil.NOT_FOUND_ADDRESS;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberQueryRepository memberQueryRepository;
    private final MemberAddressQueryRepository memberAddressQueryRepository;

    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CacheService cacheService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long saveMember(SignUpMemberRequest request) {
        validateIsExists(request.getEmail(), request.getPhone());

        String authString = (String) cacheService.get(MyCacheType.SIGN_UP_AUTH_STRING, request.getEmail());

        if (!StringUtils.hasText(authString))
            throw new IllegalArgumentException("유효하지 않는 인증문자");

        if (!request.getAuthString().equals(authString))
            throw new IllegalArgumentException("인증문자 불일치");

        cacheService.evict(MyCacheType.SIGN_UP_AUTH_STRING, request.getEmail());

        String password = bCryptPasswordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .email(request.getEmail()).phone(request.getPhone())
                .nickname(request.getNickname()).password(password)
                .provider(Provider.LOCAL).authority(Authority.MEMBER)
                .build();
        memberRepository.save(member);

        return member.getId();
    }


    //개인정보 업데이트
    public void updateMemberInformation(Long memberId, Integer height, Integer weight) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessageUtil.NOT_FOUND_MEMBER));
        member.changeInformation(height, weight);
    }

    public void saveMemberAddress(Long memberId, String name, String receiverNickname, String receiverPhone, String zipCode, String street, String detail) {
        if (memberAddressQueryRepository.existsByMemberIdAndZipCodeAndStreetAndDetail(memberId, zipCode, street, detail))
            throw new IllegalArgumentException("이미 등록된 주소입니다.");

        Member member = memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessageUtil.NOT_FOUND_MEMBER));

        boolean isDefault = !memberAddressQueryRepository.existsByMemberId(memberId);
        memberAddressRepository.save(new MemberAddress(name, receiverNickname, receiverPhone, zipCode, street, detail, member, isDefault));
    }

    public void updateMemberDefaultAddress(Long memberId, Long addressId) {
        //기존 배송지 default 해재 (false)
        MemberAddress defaultMemberAddress = memberAddressRepository.findByMemberIdAndIsDefault(memberId, true).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ADDRESS));
        defaultMemberAddress.updateIsDefault(false);

        //신규 배송지 default true
        MemberAddress memberAddress = memberAddressRepository.findById(addressId).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ADDRESS));
        memberAddress.updateIsDefault(true);
    }

    //-------------------------------------------------------------------------------------
    public void validateIsExists(String email, String phone) {
        if (memberQueryRepository.existsByEmail(email))
            throw new IllegalArgumentException("이미 등록된 이메일입니다");

        if (memberQueryRepository.existsByPhone(phone))
            throw new IllegalArgumentException("이미 등록된 핸드폰 번호입니다");
    }

    public void giveWelcomeCoupon(Long memberId) {
        try {
            Coupon coupon = new Coupon(CouponType.FIXED_DISCOUNT, 10000, LocalDate.now().plusMonths(1));
            couponRepository.save(coupon);
            //서버에서 전달하는거라 검증 불필요
            Member member = new Member(memberId);
            memberCouponRepository.save(new MemberCoupon(coupon, member, false));
        } catch (Exception e) {
            throw new RuntimeException("쿠폰 발급 실패");
        }
    }


}
