package com.seoulmilk.be.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest (
        @NotBlank
        String employeeId,

        @NotBlank
        String password
){
}
