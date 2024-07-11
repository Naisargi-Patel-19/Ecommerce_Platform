package com.ecommerce.service;


import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, Map<String, Object> model);
}
