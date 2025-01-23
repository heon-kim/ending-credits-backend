package com.hanaro.endingcredits.endingcreditsapi.domain.member.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.AssetsWishDetailDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberInfoDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberUpdateInfoDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.service.MemberService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "개인 정보 조회")
    @GetMapping("/me")
    public ApiResponseEntity<MemberInfoDto> getMyPage(@AuthenticationPrincipal UUID memberId) {
        try {
            return ApiResponseEntity.onSuccess(memberService.getMemberInfo(memberId));
        } catch (MemberHandler e) {
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @Operation(summary = "개인 정보 수정")
    @PatchMapping("/me")
    public ApiResponseEntity<MemberInfoDto> patchMyPage(@AuthenticationPrincipal UUID memberId, MemberUpdateInfoDto memberUpdateInfoDto) {
        try {
            memberService.setMemberInfo(memberId, memberUpdateInfoDto);
            return ApiResponseEntity.onSuccess(null);
        } catch (MemberHandler e) {
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @PatchMapping("/wish")
    @Operation(summary = "희망 노후 자금 저장", description = "사용자가 입력한 월 생활비, 기대수명으로 계산된 희망 노후 자금 저장")
    public ApiResponseEntity setWishFund(@AuthenticationPrincipal UUID memberId, Long wishFund) {
        try {
            memberService.setWishFund(memberId, wishFund);
            return ApiResponseEntity.onSuccess(null);
        } catch (MemberHandler e) {
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @GetMapping("/wish")
    @Operation(summary = "희망 노후 자금, 보유 자금 조회", description = "사용자의 희망 노후 자금 조회")
    public ApiResponseEntity<AssetsWishDetailDto> getAssetsWishDetail(@AuthenticationPrincipal UUID memberId) {
        try {
            return ApiResponseEntity.onSuccess(memberService.getAssetsWishDetail(memberId));
        } catch (MemberHandler e) {
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @GetMapping("/connected")
    @Operation(summary = "개인 자산 연결 여부 확인")
    public ApiResponseEntity<Boolean> isAssetsConnected(@AuthenticationPrincipal UUID memberId) {
        try {
            return ApiResponseEntity.onSuccess(memberService.isAssetsConnected(memberId));
        } catch (MemberHandler e){
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }
}

