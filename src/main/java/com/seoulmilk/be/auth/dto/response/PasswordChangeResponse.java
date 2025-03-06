package com.seoulmilk.be.auth.dto.response;

import lombok.Builder;

@Builder
public record PasswordChangeResponse (
        String uuid
) {
}
