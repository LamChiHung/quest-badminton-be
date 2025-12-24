package com.quest.badminton.service.impl;

import com.quest.badminton.entity.MailTemplate;
import com.quest.badminton.repository.MailTemplateRepository;
import com.quest.badminton.service.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final MailTemplateRepository mailTemplateRepository;

    @Override
    public void send(String to, String key, Map<String, String> subjectParams ,Map<String, String> messageParams) {
        try {
            MailTemplate mailTemplate = mailTemplateRepository.findByKey(key)
                    .orElse(null);
            if (Objects.isNull(mailTemplate)) {
                log.error("Mail template not found, key: {}", key);
                return;
            }
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            String subject = mailTemplate.getSubject();
            if (Objects.nonNull(subjectParams)) {
                for (String k : subjectParams.keySet()) {
                    subject = subject.replace(k, subjectParams.get(k));
                }
            }

            String content = mailTemplate.getContent();
            if (Objects.nonNull(messageParams)) {
                for (String k : messageParams.keySet()) {
                    content = content.replace(k, messageParams.get(k));
                }
            }

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("Quest Badminton <questbmt@gmail.com>");

            helper.setText(content, true);
            mailSender.send(message);
            log.info("Send mail successfully, key: {}, to: {}", key, to);
        } catch (Exception e) {
            log.error("Error when send mail", e);
        }
    }
}
