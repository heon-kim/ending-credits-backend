package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AssetConnectionRequest {
    private List<String> bankNames;
    private List<String> securitiesCompanyNames;
    private List<String> exchangeNames;
    private UUID memberId;
}
