package com.example.guangzhouorder.controller;

import com.example.guangzhouorder.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class WebhookController {

    private final PaymentService paymentService;

    public WebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment/webhook")
    @ResponseBody
    public ResponseEntity<String> handleWebhook(HttpServletRequest request) {
        try {
            String body = request.getReader().lines().reduce("", String::concat);
            String result = paymentService.handleWebhook(body);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing webhook: " + e.getMessage());
        }
    }
}
