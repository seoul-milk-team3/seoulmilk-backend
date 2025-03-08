package com.seoulmilk.be.auth.service;

import com.seoulmilk.be.auth.dto.request.*;
import com.seoulmilk.be.auth.exception.ExistUserException;
import com.seoulmilk.be.global.jwt.application.JwtService;
import com.seoulmilk.be.global.jwt.refresh.application.RefreshTokenService;
import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.exception.UserNotFoundException;
import com.seoulmilk.be.user.persistence.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (userRepository.existsByEmployeeId(request.employeeId())) {
            throw new ExistUserException(EXIST_EMPLOYEE_ID);
        }
        validateUniqueEmail(request.email());

        User user = request.toOfficeUser(passwordEncoder);
        userRepository.save(user);
    }

    @Transactional
    public void signUpBranch(BranchSignUpRequest request) {
        if (userRepository.existsByBusinessId(request.businessId())) {
            throw new ExistUserException(EXIST_BUSINESS_ID);
        }
        validateUniqueEmail(request.email());

        User user = request.toBranchUser(passwordEncoder);
        userRepository.save(user);
    }

    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ExistUserException(EXIST_EMAIL);
        }
    }

    @Transactional
    public void officeLogin(OfficeLoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        registerAuthentication(user.getEmail(), request.password(), response);
    }

    @Transactional
    public void branchLogin(BranchLoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByBusinessId(request.businessId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        registerAuthentication(user.getEmail(), request.password(), response);
    }

    public void registerAuthentication(String email, String password, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email,
                password
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setTokenHeader(response, accessHeader, accessToken);
        jwtService.setTokenHeader(response, refreshHeader, refreshToken);
        refreshTokenService.updateToken(email, refreshToken);
    }

    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("uername (email): {}", username);
        return userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

}
