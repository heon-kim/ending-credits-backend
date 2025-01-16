package com.hanaro.endingcredits.endingcreditsapi.domain.auth.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.auth.service.AuthService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ApiResponseEntity<TokenPairResponseDto> login(LoginDto loginDto) {
        try {
            TokenPairResponseDto tokenPair = authService.generateTokenPairWithLoginDto(loginDto);
            return ApiResponseEntity.onSuccess(tokenPair);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(
                    e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponseEntity signup(SignUpDto signUpDto) {
        try{
            UUID memberId = authService.signUp(signUpDto);
            return ApiResponseEntity.onSuccess(memberId);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "회원탈퇴")
    @PostMapping("/unsubscribe")
    public ApiResponseEntity unsubscribe(@AuthenticationPrincipal UUID memberId) {
        try{
            authService.unsubscribe(memberId);
            return ApiResponseEntity.onSuccess(null);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "아이디 중복 확인")
    @PostMapping("/id")
    public ApiResponseEntity checkIdentifier(IdentifierDto identifierDto) {
        try {
            authService.checkIdentifier(identifierDto);
            return ApiResponseEntity.onSuccess("사용 가능한 아이디입니다.", null);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "JWT 재발급", description = "refresh token을 이용해 새로운 access token과 refresh token을 발급합니다.")
    @PostMapping("/reissue")
    public ApiResponseEntity<TokenPairResponseDto> refresh(TokenRefreshDto refreshDto) {
        return ApiResponseEntity.onSuccess(authService.refreshTokenPair(refreshDto.getRefreshToken()));
    }
}

