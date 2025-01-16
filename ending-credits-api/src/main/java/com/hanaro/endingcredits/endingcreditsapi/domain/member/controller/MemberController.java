package com.hanaro.endingcredits.endingcreditsapi.domain.member.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberInfoDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.service.MemberService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
}

