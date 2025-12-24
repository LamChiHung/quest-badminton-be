package com.quest.badminton.service;

import java.util.Map;

public interface MailService {
    void send(String to, String key, Map <String, String> subjectParams, Map <String, String> messageParams);
}
