package com.seoulmilk.be.auth.service;

import com.seoulmilk.be.auth.dto.request.*;
import com.seoulmilk.be.auth.dto.response.PasswordChangeResponse;
import com.seoulmilk.be.auth.exception.NonExistUserException;
import com.seoulmilk.be.auth.exception.FailEmailEncodingException;
import com.seoulmilk.be.auth.exception.InvalidUuidException;
import com.seoulmilk.be.global.jwt.application.JwtService;
import com.seoulmilk.be.global.jwt.refresh.application.RefreshTokenService;
import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.domain.type.Role;
import com.seoulmilk.be.user.exception.UserNotFoundException;
import com.seoulmilk.be.user.persistence.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.seoulmilk.be.auth.exception.errorcode.AuthErrorCode.*;
import static com.seoulmilk.be.user.exception.errorCode.UserErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;
    @Value("${jwt.access.header}")
    private String accessHeader;


    @Transactional
    public void signUpOffice(OfficeSignUpRequest request) {
        validateUniqueUser(request.employeeId(), request.email());

        User user = request.toOfficeUser(passwordEncoder);
        userRepository.save(user);
    }

    @Transactional
    public void signUpBranch(BranchSignUpRequest request) {
        validateUniqueUser(request.employeeId(), request.email());

        User user = request.toBranchUser(passwordEncoder);
        userRepository.save(user);
    }

    private void validateUniqueUser(String employeeId, String email) {
        if (userRepository.existsByEmployeeId(employeeId)) {
            throw new NonExistUserException(NON_EXIST_EMPLOYEE_ID);
        }
        if (userRepository.existsByEmail(email)) {
            throw new NonExistUserException(NON_EXIST_EMAIL);
        }
    }

    @Transactional
    public void login(LoginRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.employeeId(),
                request.password()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        String accessToken = jwtService.createAccessToken(request.employeeId());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setTokenHeader(response, accessHeader, accessToken);
        jwtService.setTokenHeader(response, refreshHeader, refreshToken);
        refreshTokenService.updateToken(user.getEmployeeId(), refreshToken);
    }

    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmployeeId(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

}
