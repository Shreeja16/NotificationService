package com.notification.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.dto.NotificationEvent;
import com.notification.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    public NotificationListener(ObjectMapper objectMapper, EmailService emailService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "notificationTopic", groupId = "notification-group")
    public void handleNotification(String messageJson) throws Exception {
        NotificationEvent event = objectMapper.readValue(messageJson, NotificationEvent.class);

        String notificationMessage = switch (event.getEventType()) {
            case "ORDER_PLACED" -> "Your order " + event.getOrderNumber() + " has been placed.";
            case "PAYMENT_SUCCESS" -> "Payment for order " + event.getOrderNumber() + " was successful.";
            case "ORDER_SHIPPED" -> "Your order " + event.getOrderNumber() + " has been shipped.";
            default -> "Update for your order " + event.getOrderNumber() + ".";
        };

        log.info("Sending to {}: {}", event.getEmail(), notificationMessage);

        // ✅ Actually send the email
        emailService.sendEmail(event.getEmail(), "Order Update", notificationMessage);
    }
}
