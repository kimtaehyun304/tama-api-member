package org.example.tamaapi.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async("emailExecutor")
    public void sendSignedUpEmail(String toMailAddr) {
        String subject = "[TAMA] 회원가입 완료 안내";
        String body = String.format("<p>TAMA 쇼핑몰에 오신 것을 환영합니다</p>");
        sendEmail(toMailAddr, subject, body);
    }

    @Async("emailExecutor")
    public void sendAuthenticationEmail(String toMailAddr, String authString) {
        String subject = "[TAMA] 회원가입 인증문자 안내";
        String body = String.format("인증문자 : %s <p>본 메일이 생성된 이유는 해당 메일로 인증하려는 시도가 있었기 때문입니다.</p>", authString);
        sendEmail(toMailAddr, subject, body);
    }

    //@Async는 aop 라 여기에 붙이면 내부 호출이라 동작 불가
    @Retryable(backoff = @Backoff(delay = 3000, multiplier = 2), recover = "recover")
    public void sendEmail(String toMailAddr, String subject, String body) {
        //Retryable 동작을 위헤 일부로 try catch x
        MimeMessagePreparator mimeMessagePreparator = createMimeMessagePreparator(toMailAddr, subject, body);
        javaMailSender.send(mimeMessagePreparator);
    }

    private MimeMessagePreparator createMimeMessagePreparator(String toMailAddr, String subject, String body) {
        return mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(toMailAddr);
            helper.setSubject(subject);
            helper.setText(body, true);
        };
    }

    @Recover
    //파라미터 안 필요해도 비동기 메서드 파라미터랑 일치 해야 동작
    public void recover(Exception e, String toMailAddr, String subject, String body) {
        log.error("[Retry 실패] 메일 발송을 실패했습니다. toMailAddr={}, 원인={}", toMailAddr, e.getMessage());
    }

}
