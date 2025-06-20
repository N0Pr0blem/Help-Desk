package com.praktica.HelpDesk.service;

public interface MailService {
    void sendSimpleMail(String to, String subject, String text);

    void sendActivationCodeForm(String to, String code);

    void sendInformationForm(String to, String text);
}
