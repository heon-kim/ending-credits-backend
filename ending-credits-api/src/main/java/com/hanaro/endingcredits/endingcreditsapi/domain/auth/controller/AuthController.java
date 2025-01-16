package com.hanaro.endingcredits.endingcreditsapi.domain.auth.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.auth.service.AuthService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponseEntity<TokenPairResponseDto> login(LoginForm loginForm) {
        try {
            TokenPairResponseDto tokenPair = authService.generateTokenPairWithLoginForm(loginForm);
            return ApiResponseEntity.onSuccess(tokenPair);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(
                    e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponseEntity signup(SignUpForm signUpForm) {
        try{
            UUID memberId = authService.signUp(signUpForm);
            return ApiResponseEntity.onSuccess(memberId);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "회원탈퇴")
    @PostMapping("/unsubscribe")
    public ApiResponseEntity unsubscribe() {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UUID memberId = UUID.fromString(auth.getPrincipal().toString());
            authService.unsubscribe(memberId);
            return ApiResponseEntity.onSuccess(null);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "아이디 중복 확인")
    @PostMapping("/id")
    public ApiResponseEntity checkIdentifier(IdentifierForm identifierForm) {
        try {
            authService.checkIdentifier(identifierForm);
            return ApiResponseEntity.onSuccess("사용 가능한 아이디입니다.", null);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "JWT 재발급", description = "refresh token을 이용해 새로운 access token과 refresh token을 발급합니다.")
    @PostMapping("/reissue")
    public ApiResponseEntity<TokenPairResponseDto> refresh(TokenRefreshForm refreshForm) {
        return ApiResponseEntity.onSuccess(authService.refreshTokenPair(refreshForm.getRefreshToken()));
    }
}

