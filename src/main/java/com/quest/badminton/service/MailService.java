package com.quest.badminton.service;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface MailService {
    @Async
    void sendAsync(String to, String key, Map<String, String> subjectParams, Map<String, String> messageParams);

    void send(String to, String key, Map <String, String> subjectParams, Map <String, String> messageParams);
}
