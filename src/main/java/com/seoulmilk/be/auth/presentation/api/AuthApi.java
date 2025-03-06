package com.seoulmilk.be.auth.presentation.api;

import com.seoulmilk.be.auth.dto.request.*;
import com.seoulmilk.be.auth.dto.response.PasswordChangeResponse;
import com.seoulmilk.be.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Auth", description = "사용자 인증 관련 API")
public interface AuthApi {
    @Operation(
            summary = "본사 직원 회원가입",
            description = "사용자의 정보를 입력받아 회원가입을 진행합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입이 성공적으로 완료되었습니다."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "회원 정보가 중복됩니다."
            )
    })
    SuccessResponse<String> signUpOffice(OfficeSignUpRequest request);

    @Operation(
            summary = "가맹점 직원 회원가입",
            description = "사용자의 정보를 입력받아 회원가입을 진행합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입이 성공적으로 완료되었습니다."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "회원 정보가 중복됩니다."
            )
    })
    SuccessResponse<String> signUpBranch(BranchSignUpRequest request);

    @Operation(
            summary = "로그인",
            description = "사용자의 사번과 비밀번호를 입력받아 로그인합니다." + "\n" +
                    """
                            예시) 테스트 계정 
                            - 사번: test1
                            - 비밀번호: 1234
                            - 권한 : 관리자
                                    """
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


    @Operation(
            summary = "비밀번호 변경을 위한 이메일 요청",
            description = "사용자의 이메일을 입력받아 비밀번호 변경 이메일을 송신합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "비밀번호 재설정 이메일이 성공적으로 전송되었습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "이메일 인코딩이 실패했습니다."
            )
    })
    SuccessResponse<PasswordChangeResponse> sendPasswordChangeEmail(PasswordChangeRequest request);

    @Operation(
            summary = "비밀번호 변경",
            description = "비밀번호를 변경합니다. 사용자가 받은 이메일에 비밀번호 변경 링크가 있습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "비밀번호 재설정 이메일이 성공적으로 전송되었습니다."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "UUID가 유효하지 않습니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 유저가 존재하지 않습니다."
            )
    })
    SuccessResponse<String> changePassword(NewPasswordRequest request);
}
