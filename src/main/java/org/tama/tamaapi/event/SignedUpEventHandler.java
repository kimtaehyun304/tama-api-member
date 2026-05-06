package org.tama.tamaapi.event;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.tama.tamaapi.domain.user.Member;
import org.tama.tamaapi.query.MemberQueryRepository;
import org.tama.tamaapi.command.EmailService;
import org.tama.tamaapi.command.MemberService;
import org.tama.tamaapi.exception.ErrorMessageUtil;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignedUpEventHandler {

    private final MemberQueryRepository memberQueryRepository;
    private final EmailService emailService;
    private final MemberService memberService;

    @EventListener
    public void sendEmail(SignedUpEvent event) {
        //종속성 분리를 위하여, 조회하여 이메일 가져옴
        Member member = memberQueryRepository.findById(event.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessageUtil.NOT_FOUND_MEMBER));
        if (!StringUtils.hasText(member.getEmail()))
            throw new IllegalArgumentException("등록된 이메일이 아닙니다");

        emailService.sendSignedUpEmail(member.getEmail());
    }

    @EventListener
    //동기 이벤트라 예외 던지면 공통 예외 처리 가능
    public void giveWelcomeCoupon(SignedUpEvent event) {
        memberService.giveWelcomeCoupon(event.getMemberId());
    }
}
