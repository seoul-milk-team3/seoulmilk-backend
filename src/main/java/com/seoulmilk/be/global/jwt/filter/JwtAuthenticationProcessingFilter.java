package com.seoulmilk.be.global.jwt.filter;

import com.seoulmilk.be.global.jwt.application.JwtService;
import com.seoulmilk.be.global.jwt.exception.InvalidTokenException;
import com.seoulmilk.be.global.jwt.exception.JwtErrorCode;
import com.seoulmilk.be.global.jwt.refresh.application.RefreshTokenService;
import com.seoulmilk.be.global.jwt.refresh.domain.RefreshToken;
import com.seoulmilk.be.user.persistence.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String NO_CHECK_URL = "/auth/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        if (jwtService.isTokenValid(refreshToken)) {
            RefreshToken refresh = refreshTokenService.findByToken(refreshToken);
            jwtService.sendAccessAndRefreshToken(response, refresh.getEmployeeId());
        }
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                   FilterChain filterChain) throws ServletException, IOException {
        try {
            jwtService.extractAccessToken(request)
                    .ifPresent(accessToken -> jwtService.extractEmployeeId(accessToken)
                            .ifPresentOrElse(email -> userRepository.findByEmployeeId(email).ifPresent(this::saveAuthentication),
                                    () -> {
                                        throw new InvalidTokenException(JwtErrorCode.INVALID_TOKEN);
                                    }
                            )
                    );
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(com.seoulmilk.be.user.domain.User user) {
        UserDetails userDetailsUser = User.builder()
                .username(user.getEmployeeId())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

