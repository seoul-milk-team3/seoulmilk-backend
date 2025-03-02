package com.seoulmilk.be.global.jwt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "AccessToken, RefreshToken 반환 포맷")
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}