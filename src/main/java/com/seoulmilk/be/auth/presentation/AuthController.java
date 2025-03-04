package com.seoulmilk.be.auth.presentation;


import com.seoulmilk.be.auth.dto.request.LoginRequest;
import com.seoulmilk.be.auth.dto.request.OfficeSignUpRequest;
import com.seoulmilk.be.auth.presentation.api.AuthApi;
import com.seoulmilk.be.auth.service.AuthService;
import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.auth.dto.request.BranchSignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.seoulmilk.be.global.dto.SuccessCode.LOGIN_SUCCESS;
import static com.seoulmilk.be.global.dto.SuccessCode.SIGN_UP_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/office/sign-up")
    @Override
    public SuccessResponse<String> signUpOffice(@RequestBody OfficeSignUpRequest request) {
        authService.signUp(request);
        return SuccessResponse.of(SIGN_UP_SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public SuccessResponse<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        authService.login(request, response);
        return SuccessResponse.of(LOGIN_SUCCESS);
    }
}
