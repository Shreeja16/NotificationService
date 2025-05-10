package com.notification.controller;

import com.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping
    public String sendTestEmail(@RequestParam String to) {
        emailService.sendEmail(to, "Test Notification", "This is a test email from Notification Service.");
        return "Email triggered, check logs and inbox.";
    }
}
