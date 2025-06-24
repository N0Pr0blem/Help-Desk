package com.praktica.HelpDesk.service.impl;

import com.praktica.HelpDesk.exception.ApiException;
import com.praktica.HelpDesk.service.MailService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.properties.thread_count}")
    private int COUNT_OF_THREADS_FOR_MAIL_SERVICE;

    private final String EMAIL_FROM = "helpDesk@miniuser.ru";
    private final String PATH_TO_ACTIVATION_FORM_TEMPLATE = "/opt/app/src/main/resources/static/html/verification-template.html";
    private final String PATH_TO_INFORMATION_FORM_TEMPLATE = "/opt/app/src/main/resources/static/html/text-information-template.html";

    private final JavaMailSender mailSender;
    private ExecutorService emailExecutor;

    @PostConstruct
    public void init() {
        emailExecutor = Executors.newFixedThreadPool(COUNT_OF_THREADS_FOR_MAIL_SERVICE);
    }

    @Override
    public void sendSimpleMail(String to, String subject, String text) {
        emailExecutor.submit(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(EMAIL_FROM);

            mailSender.send(message);
        });
    }

    @Override
    public void sendActivationCodeForm(String to, String code) {
        emailExecutor.submit(() -> {
            try {
                String template = new String(Files.readAllBytes(Paths.get(PATH_TO_ACTIVATION_FORM_TEMPLATE)));

                String filledTemplate = fillCodeInTemplate(template, code);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(to);
                helper.setSubject("Activation code");
                helper.setFrom(EMAIL_FROM);
                helper.setText(filledTemplate, true);

                mailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApiException("Failed to send email", "SEND_EMAIL_EXCEPTION");
            }
        });
    }

    @Override
    public void sendInformationForm(String to, String text) {
        emailExecutor.submit(() -> {
            try {
                String template = new String(Files.readAllBytes(Paths.get(PATH_TO_INFORMATION_FORM_TEMPLATE)));
                template = template.replace("${text}", text);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(to);
                helper.setSubject("Information");
                helper.setFrom(EMAIL_FROM);
                helper.setText(template, true);

                mailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApiException("Failed to send email", "SEND_EMAIL_EXCEPTION");
            }
        });
    }

    private String fillCodeInTemplate(String template, String code) {
        char[] codeArray = code.toCharArray();

        for (int i = 0; i < codeArray.length; i++) {
            template = template.replace("${code[" + i + "]}", String.valueOf(codeArray[i]));
        }
        return template;
    }

    @PreDestroy
    public void shutdownExecutor() {
        emailExecutor.shutdown();
    }
}
