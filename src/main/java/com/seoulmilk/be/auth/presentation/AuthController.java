package com.seoulmilk.be.auth.presentation;


import com.seoulmilk.be.auth.dto.request.*;
import com.seoulmilk.be.auth.dto.response.PasswordChangeResponse;
import com.seoulmilk.be.auth.presentation.api.AuthApi;
import com.seoulmilk.be.auth.service.AuthService;
import com.seoulmilk.be.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.seoulmilk.be.global.dto.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/office/sign-up")
    @Override
    public SuccessResponse<String> signUpOffice(@RequestBody OfficeSignUpRequest request) {
        authService.signUpOffice(request);
        return SuccessResponse.of(SIGN_UP_SUCCESS);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/branch/sign-up")
    @Override
    public SuccessResponse<String> signUpBranch(@RequestBody BranchSignUpRequest request) {
        authService.signUpBranch(request);
        return SuccessResponse.of(SIGN_UP_SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    @Override
    public SuccessResponse<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        authService.login(request, response);
        return SuccessResponse.of(LOGIN_SUCCESS);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/password")
    @Override
    public SuccessResponse<PasswordChangeResponse> sendPasswordChangeEmail(
            @RequestBody PasswordChangeRequest request) {
        return SuccessResponse.of(SEND_PASSWORD_CHANGE_EMAIL_SUCCESS,
                authService.sendPasswordChangeEmail(request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/password")
    public SuccessResponse<String> changePassword(@RequestBody NewPasswordRequest request) {
        authService.changePassword(request);
        return SuccessResponse.of(SEND_PASSWORD_CHANGE_EMAIL_SUCCESS);
    }
}
