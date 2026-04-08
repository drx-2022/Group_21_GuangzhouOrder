package com.example.guangzhouorder.controller;

import com.example.guangzhouorder.entity.Order;
import com.example.guangzhouorder.entity.User;
import com.example.guangzhouorder.repository.OrderRepository;
import com.example.guangzhouorder.repository.OrderTrackingRepository;
import com.example.guangzhouorder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardController {

    private final OrderRepository orderRepository;
    private final OrderTrackingRepository orderTrackingRepository;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        
        // Check if user is admin
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }

        // Get all orders
        List<Order> allOrders = orderRepository.findAll();
        
        // Calculate statistics
        long totalOrders = allOrders.size();
        long draftOrders = allOrders.stream()
                .filter(o -> "DRAFT".equals(o.getStatus())).count();
        long negotiatingOrders = allOrders.stream()
                .filter(o -> "NEGOTIATING".equals(o.getStatus())).count();
        long inProductionOrders = allOrders.stream()
                .filter(o -> "IN_PRODUCTION".equals(o.getStatus())).count();
        long pendingApprovalOrders = allOrders.stream()
                .filter(o -> "PENDING_CUSTOMER_APPROVAL".equals(o.getStatus())).count();
        long readyForShippingOrders = allOrders.stream()
                .filter(o -> "READY_FOR_SHIPPING".equals(o.getStatus())).count();
        long doneOrders = allOrders.stream()
                .filter(o -> "DONE".equals(o.getStatus())).count();
        long cancelledOrders = allOrders.stream()
                .filter(o -> "CANCELLED".equals(o.getStatus())).count();
        
        // Payment statistics
        long unpaidOrders = allOrders.stream()
                .filter(o -> o.getPaymentStatus() == null || "UNPAID".equals(o.getPaymentStatus()))
                .count();
        long depositedOrders = allOrders.stream()
                .filter(o -> "DEPOSITED".equals(o.getPaymentStatus())).count();
        long paidOrders = allOrders.stream()
                .filter(o -> "DONE".equals(o.getPaymentStatus())).count();

        // Enrich orders with tracking info
        var ordersWithTracking = allOrders.stream()
                .map(order -> {
                    var tracking = orderTrackingRepository.findByOrderAndIsCurrent(order, true);
                    return new OrderWithTracking(
                            order,
                            tracking.orElse(null)
                    );
                })
                .collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("allOrders", ordersWithTracking);
        model.addAttribute("totalOrders", totalOrders);
        
        // Status counts
        model.addAttribute("draftOrders", draftOrders);
        model.addAttribute("negotiatingOrders", negotiatingOrders);
        model.addAttribute("inProductionOrders", inProductionOrders);
        model.addAttribute("pendingApprovalOrders", pendingApprovalOrders);
        model.addAttribute("readyForShippingOrders", readyForShippingOrders);
        model.addAttribute("doneOrders", doneOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);
        
        // Payment counts
        model.addAttribute("unpaidOrders", unpaidOrders);
        model.addAttribute("depositedOrders", depositedOrders);
        model.addAttribute("paidOrders", paidOrders);

        return "admin/dashboard";
    }

    // Helper class to pass order with current tracking
    public static class OrderWithTracking {
        public final Order order;
        public final com.example.guangzhouorder.entity.OrderTracking currentTracking;

        public OrderWithTracking(Order order, com.example.guangzhouorder.entity.OrderTracking currentTracking) {
            this.order = order;
            this.currentTracking = currentTracking;
        }

        public Order getOrder() {
            return order;
        }

        public com.example.guangzhouorder.entity.OrderTracking getCurrentTracking() {
            return currentTracking;
        }
    }
}
