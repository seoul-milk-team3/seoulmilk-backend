package com.seoulmilk.be.auth.dto.request;

import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.domain.type.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record OfficeSignUpRequest (
        @Schema(description = "본명이어야 합니다.")
        @NotBlank
        String name,

        @Schema(description = "사번입니다.")
        @NotBlank
        String employeeId,

        @NotBlank
        String password,

        @NotBlank
        String email,

        @NotBlank
        String phoneNo,
        @Schema(description = "생년월일 입니다. (형식: YYYYMMDD)")
        @NotBlank
        String birthday,

        @Schema(description = "통신사입니다.")
        @NotBlank
        String telecom,

        @Schema(description = "ADMIN: 관리자, OFFICE: 본사 직원, BRANCH: 대리점 직원")
        @NotBlank
        Role role
) {
    public User toOfficeUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(this.name)
                .employeeId(this.employeeId)
                .password(passwordEncoder.encode(this.password))
                .email(this.email)
                .phoneNo(phoneNo)
                .birthday(this.birthday)
                .telecom(this.telecom)
                .role(this.role)
                .build();
    }
}
