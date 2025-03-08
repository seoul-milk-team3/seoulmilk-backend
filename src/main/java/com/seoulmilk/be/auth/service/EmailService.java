package com.seoulmilk.be.auth.service;

import com.seoulmilk.be.auth.exception.FailEmailEncodingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.seoulmilk.be.auth.exception.errorcode.AuthErrorCode.EMAIL_ENCODING_FAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(String toEmail, String passwordUrl) {
        String title = "[서울우유 비밀번호 재설정] 서울우유 세금계산서 시스템 비밀번호 재설정 링크입니다.";
        String content = """
                서울우유 세금계산서 검증 시스템
                <a href="%s">%s</a>
                <br><br>
                해당 링크는 30분 동안만 유효합니다.<br>
                """.formatted(passwordUrl, passwordUrl);

        sendEmail(toEmail, title, content);
    }

    private void sendEmail(String toEmail, String title, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(new InternetAddress(fromEmail, "서울우유 세금계산서 검증 시스템"));
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new FailEmailEncodingException(EMAIL_ENCODING_FAIL);
        }
    }
}