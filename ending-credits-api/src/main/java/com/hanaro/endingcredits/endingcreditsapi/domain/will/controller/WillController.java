package com.hanaro.endingcredits.endingcreditsapi.domain.will.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.ExecutorDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.FinalMessageDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.InheritanceDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.PurposeDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.service.WillService;
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

    @PostMapping("/speech/purpose")
    public ResponseEntity<PurposeDto> extractWillPurpose(@RequestParam String fileUrl) {
        PurposeDto purpose = willService.extractWillPurpose(fileUrl);
        return ResponseEntity.ok(purpose);
    }

    @PostMapping("/speech/inheritance")
    public ResponseEntity<InheritanceDto[]> extractWillInheritance(@RequestParam String fileUrl, @RequestParam String type) {
        InheritanceDto[] inheritances = willService.extractWillInheritance(fileUrl, type);
        return ResponseEntity.ok(inheritances);
    }

    @PostMapping("/speech/executor")
    public ResponseEntity<ExecutorDto[]> extractWillExecutor(@RequestParam String fileUrl) {
        ExecutorDto[] executors = willService.extractWillExecutor(fileUrl);
        return ResponseEntity.ok(executors);
    }

    @PostMapping("/speech/final-message")
    public ResponseEntity<FinalMessageDto[]> extractWillFinalMessage(@RequestParam String fileUrl) {
        FinalMessageDto[] finalMessages = willService.extractWillFinalMessage(fileUrl);
        return ResponseEntity.ok(finalMessages);
    }

    @PostMapping("/speech/confirmation")
    public ResponseEntity<Boolean> extractWillConfirmation(@RequestParam String fileUrl) {
        Boolean confirmation = willService.extractWillConfirmation(fileUrl);
        return ResponseEntity.ok(confirmation);
    }
}
