package com.seoulmilk.be.auth.dto.request;

import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.domain.type.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record BranchSignUpRequest(
        @NotBlank
        String name,

        @Schema(description = "사번입니다.")
        @NotBlank
        String employeeId,

        @NotBlank
        String password,

        @NotBlank
        String email
) {
    public User toBranchUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(this.name)
                .employeeId(this.employeeId)
                .password(passwordEncoder.encode(this.password))
                .email(this.email)
                .role(Role.BRANCH)
                .build();
    }
}
