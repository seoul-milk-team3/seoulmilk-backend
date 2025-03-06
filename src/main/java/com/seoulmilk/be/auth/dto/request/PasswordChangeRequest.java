package com.seoulmilk.be.auth.dto.request;

import com.seoulmilk.be.user.domain.type.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "비밀번호 변경을 위한 이메일 요청 DTO")
@Builder
public record PasswordChangeRequest(
        @Schema(description = "수신인 이메일 주소")
        @NotBlank
        String email,

        @Schema(description = "대리점 / 본사 여부")
        @NotBlank
        Role role
) {

}
