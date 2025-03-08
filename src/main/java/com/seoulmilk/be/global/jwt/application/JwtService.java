package com.seoulmilk.be.global.jwt.application;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.be.global.jwt.dto.response.TokenResponse;
import com.seoulmilk.be.global.jwt.exception.TokenCreateException;
import com.seoulmilk.be.global.jwt.refresh.application.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static com.seoulmilk.be.global.jwt.exception.JwtErrorCode.TOKEN_CREATE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String BEARER = "Bearer ";
    private static final String EMAIL_CLAIM = "email";

    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;


    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject("RefreshToken")
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String email) throws TokenCreateException {
        String accessToken = createAccessToken(email);
        String refreshToken = createRefreshToken();

        try {
            String token = objectMapper.writeValueAsString(TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build());
            response.getWriter().write(token);
        } catch (IOException e) {
            throw new TokenCreateException(TOKEN_CREATE_ERROR);
        }

        setTokenHeader(response, accessHeader, accessToken);
        setTokenHeader(response, refreshHeader, refreshToken);
        refreshTokenService.updateToken(email, refreshToken);
    }

    public void setTokenHeader(HttpServletResponse response, String headerName, String token) {
        response.setHeader(headerName, BEARER + token);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractEmail(String accessToken) throws JWTVerificationException {
        try {
            return Optional.ofNullable(
                    JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken).getClaim(EMAIL_CLAIM).asString());
        } catch (JWTVerificationException e) {
            log.error("access token is not valid. {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("access token is not valid. {}", e.getMessage());
            return false;
        }
    }
}
