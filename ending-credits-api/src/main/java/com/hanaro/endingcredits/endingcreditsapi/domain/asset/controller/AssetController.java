package com.hanaro.endingcredits.endingcreditsapi.domain.asset.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.AssetConnectionRequest;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.AssetConnectionService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {
    private final AssetConnectionService assetConnectionService;

    @PostMapping("/connect/all")
    public ResponseEntity<ApiResponseEntity<String>> connectAllAssets(
            @RequestBody AssetConnectionRequest request) {
        assetConnectionService.connectAllAssets(
                request.getBankNames(),
                request.getSecuritiesCompanyNames(),
                request.getExchangeNames(),
                request.getMemberId()
        );
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("모든 자산 연결 완료", null));
    }
}
