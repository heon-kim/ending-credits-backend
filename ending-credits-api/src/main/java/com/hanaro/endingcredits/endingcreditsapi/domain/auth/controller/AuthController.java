package com.hanaro.endingcredits.endingcreditsapi.domain.auth.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.auth.service.AuthService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.JwtHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.VerificationHandler;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.apache.http.HttpHeaders;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponseEntity<LoginResponseDto> login(LoginDto loginDto) {
        try {
            LoginResponseDto tokenPair = authService.generateTokenPairWithLoginDto(loginDto);
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
    public ApiResponseEntity checkIdentifier(String identifier) {
        try {
            authService.checkIdentifier(identifier);
            return ApiResponseEntity.onSuccess("사용 가능한 아이디입니다.", null);
        } catch (MemberHandler e) {
            // MemberHandler 예외 처리
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "JWT 재발급", description = "refresh token을 이용해 새로운 access token과 refresh token을 발급합니다.")
    @PostMapping("/reissue")
    public ApiResponseEntity<TokenPairResponseDto> refresh(String refreshToken) {
        try{
            return ApiResponseEntity.onSuccess(authService.refreshTokenPair(refreshToken));
        }catch (JwtHandler e) {
            // JwtHandler 예외 처리
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "비밀번호 재설정")
    @PatchMapping("/password")
    public ApiResponseEntity changePassword(String phoneNumber, String password) {
        try{
            authService.changePassword(phoneNumber, password);
            return ApiResponseEntity.onSuccess(null);
        } catch(MemberHandler e) {
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "간편 비밀번호 재설정")
    @PatchMapping("/simple-password")
    public ApiResponseEntity changeSimplePassword(String identifier, String simplePassword) {
        authService.changeSimplePassword(identifier, simplePassword);
        return ApiResponseEntity.onSuccess(null);
    }

    @Operation(summary = "카카오 로그인")
    @GetMapping("/klogin")
    public ResponseEntity<Object> kakaoLogin() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + authService.getKakaoClientId() +
                "&redirect_uri=" + authService.getKakaoRedirectUri() +
                "&response_type=code";

        // 클라이언트를 카카오 인증 URL로 리다이렉트
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, kakaoAuthUrl)
                .build();
    }

    @Operation(summary = "카카오 로그인 콜백")
    @GetMapping("/kakao")
    public ResponseEntity<ApiResponseEntity<TokenPairResponseDto>>  kakaoLoginCallback(@RequestParam String code, HttpServletResponse response) {
        try {
            LoginResponseDto loginResponseDto = authService.processKakaoLogin(code);

            // Access Token Cookie 설정
            Cookie accessTokenCookie = new Cookie("accessToken", loginResponseDto.getTokenPair().getAccessToken());
            accessTokenCookie.setHttpOnly(false); // 필요에 따라 HttpOnly 설정
            accessTokenCookie.setSecure(false);   // 개발 환경에서는 Secure=false
            accessTokenCookie.setPath("/");       // 모든 경로에서 접근 가능

            // Refresh Token Cookie 설정
            Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponseDto.getTokenPair().getRefreshToken());
            refreshTokenCookie.setHttpOnly(false);
            refreshTokenCookie.setSecure(false);
            refreshTokenCookie.setPath("/");

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            // 리다이렉트 처리
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "https://localhost:5173/") // 프론트엔드 URL
                    .build();
        } catch (MemberHandler e) {
            return ResponseEntity.status(HttpStatus.FOUND) // 302로 리다이렉트 처리
                    .header(HttpHeaders.LOCATION, "https://localhost:5173/signup")
                    .body(ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null));
        }
    }

    @Operation(summary = "신분증 OCR")
    @PostMapping("/id-card")
    public ApiResponseEntity<IdCardDto> recognizeIdCard(@RequestParam("file") MultipartFile file) {
        IdCardDto idCard = authService.recognizeIdCard(file);
        return ApiResponseEntity.onSuccess(idCard);
    }

    @Operation(summary = "본인인증 SMS 전송")
    @PostMapping("/send-sms")
    public ApiResponseEntity sendSms(String phoneNumber) {
        try {
            authService.sendSms(phoneNumber);
            return ApiResponseEntity.onSuccess(null);
        } catch (VerificationHandler e) {
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "본인인증 SMS 코드 인증")
    @PostMapping("/verify-sms")
    public ApiResponseEntity sendSms(String phoneNumber, String certificationCode) {
        try {
            authService.verifySms(phoneNumber, certificationCode);
            return ApiResponseEntity.onSuccess(null);
        } catch (VerificationHandler e) {
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }
}

