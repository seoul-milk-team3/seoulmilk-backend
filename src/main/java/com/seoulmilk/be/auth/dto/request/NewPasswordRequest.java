package com.seoulmilk.be.auth.dto.request;

import lombok.Builder;

@Builder
public record NewPasswordRequest(
        String uuid,
        String password
) {
}
