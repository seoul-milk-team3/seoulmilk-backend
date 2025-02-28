package com.seoulmilk.be.auth.presentation.api;

import com.seoulmilk.be.auth.dto.request.LoginRequest;
import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.auth.dto.request.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Auth", description = "사용자 인증 관련 API")
public interface AuthApi {
    @Operation(
            summary = "회원가입",
            description = "사용자의 정보를 입력받아 회원가입을 진행합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입이 성공적으로 완료되었습니다."
            ),
            @ApiResponse(
                    responseCode = "208",
                    description = "The employee ID already exists."
            ),
            @ApiResponse(
                    responseCode = "208",
                    description = "The email already exists."
            )
    })
    SuccessResponse<String> signUp(SignUpRequest request);

    @Operation(
            summary = "로그인",
            description = "사용자의 사번과 비밀번호를 입력받아 로그인합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인이 성공적으로 완료되었습니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    SuccessResponse<String> login(LoginRequest request, HttpServletResponse response);
}
