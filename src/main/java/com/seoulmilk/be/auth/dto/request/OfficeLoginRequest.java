package com.seoulmilk.be.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record OfficeLoginRequest (
        @NotBlank
        String employeeId,

        @NotBlank
        String password
){
}
