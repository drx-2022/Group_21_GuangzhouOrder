package com.example.guangzhouorder.controller;

import com.example.guangzhouorder.entity.ProductCard;
import com.example.guangzhouorder.entity.User;
import com.example.guangzhouorder.repository.ProductCardRepository;
import com.example.guangzhouorder.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductCardRepository productCardRepository;
    private final UserService userService;

    public HomeController(ProductCardRepository productCardRepository, UserService userService) {
        this.productCardRepository = productCardRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<ProductCard> featuredProducts = productCardRepository.findTop6ByIsPublicTrue();
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("pageTitle", "Guangzhou Direct | Source from China");
        
        // Add current user if authenticated
        if (userDetails != null) {
            User currentUser = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("currentUser", currentUser);
        }
        
        return "public/home";
    }

    @GetMapping("/sourcing-service")
    public String sourcingService(Model model) {
        model.addAttribute("pageTitle", "Sourcing Service | Guangzhou Direct");
        return "public/sourcing_service";
    }

    @GetMapping("/logistics-hub")
    public String logisticsHub(Model model) {
        model.addAttribute("pageTitle", "Logistics Hub | Guangzhou Direct");
        return "public/logistics_hub";
    }

    @GetMapping("/api-integration")
    public String apiIntegration(Model model) {
        model.addAttribute("pageTitle", "API Integration | Guangzhou Direct");
        return "public/api_integration";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("pageTitle", "Terms of Service | Guangzhou Direct");
        return "public/terms";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("pageTitle", "Privacy Policy | Guangzhou Direct");
        return "public/privacy";
    }

    @GetMapping("/sourcing-guide")
    public String sourcingGuide(Model model) {
        model.addAttribute("pageTitle", "Sourcing Guide | Guangzhou Direct");
        return "public/sourcing_guide";
    }
}
