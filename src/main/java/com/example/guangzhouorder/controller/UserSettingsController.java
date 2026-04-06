package com.example.guangzhouorder.controller;

import com.example.guangzhouorder.dto.ChangePasswordDTO;
import com.example.guangzhouorder.dto.UserSettingsDTO;
import com.example.guangzhouorder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserService userService;

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute UserSettingsDTO dto,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {
        userService.updateProfile(userDetails.getUsername(), dto.getName(), dto.getPhone());
        redirectAttributes.addFlashAttribute("settingsSuccess", "Profile updated successfully.");
        return "redirect:" + getPreviousPage(request); // redirect back to their dashboard
    }

    @PostMapping("/send-otp")
    @ResponseBody
    public ResponseEntity<?> sendOtp(@AuthenticationPrincipal UserDetails userDetails) {
        userService.sendPasswordChangeOtp(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "OTP sent to your email."));
    }

    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestBody ChangePasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Passwords do not match."));
        }
        try {
            userService.changePasswordWithOtp(userDetails.getUsername(), dto.getOtpCode(), dto.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Password changed successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private String getPreviousPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return (referer != null) ? referer : "/";
    }
}
