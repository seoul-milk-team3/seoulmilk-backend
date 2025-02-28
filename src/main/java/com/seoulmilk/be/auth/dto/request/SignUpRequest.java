package com.seoulmilk.be.auth.dto.request;

import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.domain.type.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record SignUpRequest (
        String name,
        String employeeId,
        String password,
        String email,
        Role role
) {
    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(this.name)
                .employeeId(this.employeeId)
                .password(passwordEncoder.encode(this.password))
                .email(this.email)
                .role(this.role)
                .build();
    }

}
