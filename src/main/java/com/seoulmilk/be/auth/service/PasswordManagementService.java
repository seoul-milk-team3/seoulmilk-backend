package com.seoulmilk.be.auth.service;

import com.seoulmilk.be.auth.dto.request.NewPasswordRequest;
import com.seoulmilk.be.auth.dto.request.PasswordChangeRequest;
import com.seoulmilk.be.auth.dto.response.PasswordChangeResponse;
import com.seoulmilk.be.auth.exception.ExistUserException;
import com.seoulmilk.be.auth.exception.NotFoundUserException;
import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.domain.type.Role;
import com.seoulmilk.be.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.seoulmilk.be.auth.exception.errorcode.AuthErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PasswordManagementService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PasswordEmailCacheService emailCacheService;
    private final EmailService emailService;

    @Value("${email.password-url.office}")
    private String officePasswordUrl;
    @Value("${email.password-url.branch}")
    private String branchPasswordUrl;

    public PasswordChangeResponse sendPasswordChangeEmail(PasswordChangeRequest request) {
        String uuid = UUID.randomUUID().toString();
        String passwordUrl = getPasswordUrl(request.role(), uuid);

        emailService.sendPasswordResetEmail(request.email(), passwordUrl);
        saveUuidAndEmail(uuid, request.email());

        return new PasswordChangeResponse(uuid);
    }

    private String getPasswordUrl(Role role, String uuid) {
        String baseUrl;
        if (role == Role.BRANCH) {
            baseUrl = branchPasswordUrl;
        } else {
            baseUrl = officePasswordUrl;
        }
        return baseUrl + "/" + uuid;
    }

    @Transactional
    public void saveUuidAndEmail(String uuid, String email) {
        long uuidValidTime = 30 * 60 * 1000L; // 30분
        emailCacheService.setValuesWithTimeout(uuid, email, uuidValidTime);
    }

    @Transactional
    public void changePassword(NewPasswordRequest request) {
        String email = emailCacheService.getValue(request.uuid());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_USER));

        user.updatePassword(passwordEncoder.encode(request.password()));

        emailCacheService.deleteValues(request.uuid());
    }
}