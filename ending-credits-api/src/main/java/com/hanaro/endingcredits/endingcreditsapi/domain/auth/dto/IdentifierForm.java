package com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IdentifierForm {
    @NotBlank
    private String identifier;
}
