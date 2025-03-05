package com.seoulmilk.be.auth.dto.request;

import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.domain.type.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Schema(description = "대리점 회원가입 요청 DTO")
@Builder
public record BranchSignUpRequest(
        @NotBlank
        String employeeId,

        @Schema(defaultValue = "01022223333")
        @NotBlank
        String phoneNo,

        @NotBlank
        String password,

        @Schema(defaultValue = "yeonjy@seoul.com")
        @NotBlank
        String email
) {
    public User toBranchUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .employeeId(this.employeeId)
                .phoneNo(this.phoneNo)
                .password(passwordEncoder.encode(this.password))
                .email(this.email)
                .role(Role.BRANCH)
                .build();
    }
}
