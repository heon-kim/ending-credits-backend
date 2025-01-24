package com.hanaro.endingcredits.endingcreditsapi.domain.asset.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.AssetConnectionRequest;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.BankAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.SecuritiesAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.VirtualAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.AssetsLoanDetailDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.*;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;
    private final AssetConnectionService assetConnectionService;
    private final BankAssetService bankAssetService;
    private final SecuritiesAssetService securitiesAssetService;
    private final VirtualAssetService virtualAssetService;
    private final AssetAllConnectionService assetAllConnectionService;

    /**
     * 은행 자산 조회 API
     */
    @GetMapping("/bank")
    @Operation(summary = "은행 자산 조회")
    public ApiResponseEntity<List<BankAssetDto>> getBankAssets(@AuthenticationPrincipal UUID memberId) {
        List<BankAssetDto> bankAssets = bankAssetService.getConnectedBankAssets(memberId);
        return ApiResponseEntity.onSuccess("은행 자산 조회 성공", bankAssets);
    }

    /**
     * 증권 자산 조회 API
     */
    @GetMapping("/securities")
    @Operation(summary = "증권 자산 조회")
    public ApiResponseEntity<List<SecuritiesAssetDto>> getSecuritiesAssets(@AuthenticationPrincipal UUID memberId) {
        List<SecuritiesAssetDto> securitiesAssets = securitiesAssetService.getSecuritiesAssets(memberId);
        return ApiResponseEntity.onSuccess("증권 자산 조회 성공", securitiesAssets);
    }

    /**
     * 가상자산 조회 API
     */
    @GetMapping("/virtual")
    @Operation(summary = "가상자산 조회")
    public ApiResponseEntity<List<VirtualAssetDto>> getVirtualAssets(@AuthenticationPrincipal UUID memberId) {
        List<VirtualAssetDto> virtualAssets = virtualAssetService.getConnectedVirtualAssets(memberId);
        return ApiResponseEntity.onSuccess("가상자산 조회 성공", virtualAssets);
    }

    /**
     * 선택된 자산 연결 API
     */
    @PostMapping("/connect/selected")
    @Operation(summary= "선택된 개인 자산 연결", description = "선택된 개인 자산을 연결합니다.")
    public ApiResponseEntity<String> connectSelectedAssets(
            @AuthenticationPrincipal UUID memberId,
            @RequestBody AssetConnectionRequest request) {
        assetConnectionService.connectAllAssets(
                request.getBankNames(),
                request.getSecuritiesCompanyNames(),
                request.getExchangeNames(),
                memberId
        );
        return ApiResponseEntity.onSuccess("선택된 모든 자산 연결 완료", null);
    }

    /**
     * 모든 자산 연결 API
     */
    @PostMapping("/connect/all")
    @Operation(summary = "모든 개인 자산 연결", description = "모든 개인 자산을 연결합니다.")
    public ApiResponseEntity<String> connectAllAssets(
            @AuthenticationPrincipal UUID memberId
    ){
        assetAllConnectionService.connectAllAssets(memberId);
        return ApiResponseEntity.onSuccess("회원 전체 자산 연결 완료", null);
    }

    @GetMapping("/detail")
    @Operation(summary= "개인 자산 조회", description = "보유하고 있는 자산들을 조회합니다.")
    public ApiResponseEntity<AssetsLoanDetailDto> getAssetsDetail(@AuthenticationPrincipal UUID memberId) {
        try {
            AssetsLoanDetailDto assetDetails = assetService.getAssetsLoanDetail(memberId);
            return ApiResponseEntity.onSuccess(assetDetails);
        } catch (MemberHandler e){
            return ApiResponseEntity.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }
}

