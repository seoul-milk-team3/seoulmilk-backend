package com.seoulmilk.be.auth.service;

import com.seoulmilk.be.auth.dto.request.LoginRequest;
import com.seoulmilk.be.auth.exception.ExistUserException;
import com.seoulmilk.be.global.jwt.application.JwtService;
import com.seoulmilk.be.global.jwt.refresh.application.RefreshTokenService;
import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.auth.dto.request.SignUpRequest;
import com.seoulmilk.be.user.exception.UserNotFoundException;
import com.seoulmilk.be.user.persistence.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.seoulmilk.be.auth.exception.errorCode.AuthErrorCode.EXIST_EMAIL;
import static com.seoulmilk.be.auth.exception.errorCode.AuthErrorCode.EXIST_EMPLOYEE_ID;
import static com.seoulmilk.be.user.exception.errorCode.UserErrorCode.USER_NOT_FOUND;

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
    public void signUp(SignUpRequest request) {
        if (userRepository.existsByEmployeeId(request.employeeId())) {
            throw new ExistUserException(EXIST_EMPLOYEE_ID);
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ExistUserException(EXIST_EMAIL);
        }

        User user = request.toUser(passwordEncoder);
        userRepository.save(user);
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
