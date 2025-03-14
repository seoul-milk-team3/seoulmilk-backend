package com.seoulmilk.be.auth.application;

import com.seoulmilk.be.auth.domain.RefreshToken;
import com.seoulmilk.be.auth.exception.RefreshTokenNotFoundException;
import com.seoulmilk.be.auth.persistence.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.seoulmilk.be.auth.exception.errorcode.RefreshErrorCode.REFRESH_TOKEN_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration}")
    private Long expirationPeriod;

    public void updateToken(String email, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findById(email).orElse(new RefreshToken(email));
        refreshToken.createRefreshToken(token, expirationPeriod / 1000);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(REFRESH_TOKEN_NOT_FOUND));

    }
}
