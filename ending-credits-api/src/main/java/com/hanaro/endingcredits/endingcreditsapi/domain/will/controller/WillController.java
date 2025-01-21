package com.hanaro.endingcredits.endingcreditsapi.domain.will.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.ExecutorDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.FinalMessageDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.InheritanceDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.PurposeDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.service.WillService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/will")
public class WillController {
    private final WillService willService;

    @Operation(summary = "step1. 취지")
    @PostMapping("/speech/purpose")
    public ApiResponseEntity<PurposeDto> extractWillPurpose(@RequestParam String fileUrl) {
        PurposeDto purpose = willService.extractWillPurpose(fileUrl);
        return ApiResponseEntity.onSuccess(purpose);
    }

    @Operation(summary = "step2. 상속 재산")
    @PostMapping("/speech/inheritance")
    public ApiResponseEntity<InheritanceDto[]> extractWillInheritance(@RequestParam String fileUrl, @RequestParam String type) {
        InheritanceDto[] inheritances = willService.extractWillInheritance(fileUrl, type);
        return ApiResponseEntity.onSuccess(inheritances);
    }

    @Operation(summary = "step3. 집행자")
    @PostMapping("/speech/executor")
    public ApiResponseEntity<ExecutorDto[]> extractWillExecutor(@RequestParam String fileUrl) {
        ExecutorDto[] executors = willService.extractWillExecutor(fileUrl);
        return ApiResponseEntity.onSuccess(executors);
    }

    @Operation(summary = "step4. 남기고 싶은 말")
    @PostMapping("/speech/final-message")
    public ApiResponseEntity<FinalMessageDto[]> extractWillFinalMessage(@RequestParam String fileUrl) {
        FinalMessageDto[] finalMessages = willService.extractWillFinalMessage(fileUrl);
        return ApiResponseEntity.onSuccess(finalMessages);
    }

    @Operation(summary = "step5. 법적 효력")
    @PostMapping("/speech/confirmation")
    public ApiResponseEntity<Boolean> extractWillConfirmation(@RequestParam String fileUrl) {
        Boolean confirmation = willService.extractWillConfirmation(fileUrl);
        return ApiResponseEntity.onSuccess(confirmation);
    }
}
