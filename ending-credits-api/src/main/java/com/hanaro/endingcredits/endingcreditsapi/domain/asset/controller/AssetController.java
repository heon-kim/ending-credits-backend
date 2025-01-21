package com.hanaro.endingcredits.endingcreditsapi.domain.asset.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.AssetConnectionRequest;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.BankAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.SecuritiesAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.VirtualAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.AssetConnectionService;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.BankAssetService;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.SecuritiesAssetService;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.VirtualAssetService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {
    private final AssetConnectionService assetConnectionService;
    private final BankAssetService bankAssetService;
    private final SecuritiesAssetService securitiesAssetService;
    private final VirtualAssetService virtualAssetService;

    /**
     * 은행 자산 조회 API
     */
    @GetMapping("/bank")
    public ResponseEntity<ApiResponseEntity<List<BankAssetDto>>> getBankAssets(@AuthenticationPrincipal UUID memberId) {
        List<BankAssetDto> bankAssets = bankAssetService.getConnectedBankAssets(memberId);
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("은행 자산 조회 성공", bankAssets));
    }

    /**
     * 증권 자산 조회 API
     */
    @GetMapping("/securities")
    public ResponseEntity<ApiResponseEntity<List<SecuritiesAssetDto>>> getSecuritiesAssets(@AuthenticationPrincipal UUID memberId) {
        List<SecuritiesAssetDto> securitiesAssets = securitiesAssetService.getSecuritiesAssets(memberId);
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("증권 자산 조회 성공", securitiesAssets));
    }

    /**
     * 가상자산 조회 API
     */
    @GetMapping("/virtual")
    public ResponseEntity<ApiResponseEntity<List<VirtualAssetDto>>> getVirtualAssets(@AuthenticationPrincipal UUID memberId) {
        List<VirtualAssetDto> virtualAssets = virtualAssetService.getConnectedVirtualAssets(memberId);
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("가상자산 조회 성공", virtualAssets));
    }

    /**
     * 모든 자산 연결 API
     */
    @PostMapping("/connect/all")
    public ResponseEntity<ApiResponseEntity<String>> connectAllAssets(
            @AuthenticationPrincipal UUID memberId,
            @RequestBody AssetConnectionRequest request) {
        assetConnectionService.connectAllAssets(
                request.getBankNames(),
                request.getSecuritiesCompanyNames(),
                request.getExchangeNames(),
                memberId
        );
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("모든 자산 연결 완료", null));
    }
}
