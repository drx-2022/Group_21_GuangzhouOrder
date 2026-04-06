package com.example.guangzhouorder.service;

import com.example.guangzhouorder.entity.EmailVerificationToken;
import com.example.guangzhouorder.entity.OtpToken;
import com.example.guangzhouorder.entity.User;
import com.example.guangzhouorder.repository.EmailVerificationTokenRepository;
import com.example.guangzhouorder.repository.OtpTokenRepository;
import com.example.guangzhouorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public User registerUser(String name, String email, String phone, String password, String role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .hashedPassword(passwordEncoder.encode(password))
                .role(role)
                .accountVerified(false)
                .loginDisabled(false)
                .build();

        User savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .used(false)
                .user(savedUser)
                .build();

        emailVerificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(savedUser.getEmail(), token);
        return savedUser;
    }

    @Transactional
    public User verifyEmail(String token) {
        log.info("Attempting to verify token: {}", token);
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        log.info("Found token record, used={}, expiresAt={}", verificationToken.isUsed(), verificationToken.getExpiresAt());

        if (verificationToken.isUsed()) {
            throw new IllegalArgumentException("Token has already been used");
        }

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        verificationToken.setUsed(true);
        emailVerificationTokenRepository.save(verificationToken);
        log.info("Token marked as used, id={}", verificationToken.getId());

        User user = verificationToken.getUser();
        user.setAccountVerified(true);
        User savedUser = userRepository.save(user);
        log.info("User verified, userId={}", savedUser.getUserId());
        return savedUser;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isAccountVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .used(false)
                .user(user)
                .build();

        emailVerificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user.getEmail(), token);
    }
    
    // Update name and phone
    @Transactional
    public void updateProfile(String email, String newName, String newPhone) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setName(newName);
        user.setPhone(newPhone);
        userRepository.save(user);
    }

    // Generate and send OTP for password change
    @Transactional
    public void sendPasswordChangeOtp(String email) {
        // Delete any existing OTPs for this user
        otpTokenRepository.deleteAllByEmail(email);
        
        // Generate 6-digit code
        String code = String.format("%06d", new Random().nextInt(999999));
        
        OtpToken otp = new OtpToken();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setPurpose("CHANGE_PASSWORD");
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otp.setUsed(false);
        otpTokenRepository.save(otp);
        
        // Send email
        emailService.sendOtpEmail(email, code);
    }

    // Verify OTP and change password
    @Transactional
    public boolean changePasswordWithOtp(String email, String code, String newPassword) {
        OtpToken otp = otpTokenRepository
            .findTopByEmailAndPurposeAndUsedFalseOrderByIdDesc(email, "CHANGE_PASSWORD")
            .orElseThrow(() -> new RuntimeException("OTP not found"));
        
        if (otp.isUsed() || otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
        if (!otp.getCode().equals(code)) {
            throw new RuntimeException("Invalid OTP");
        }
        
        otp.setUsed(true);
        otpTokenRepository.save(otp);
        
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setHashedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}
