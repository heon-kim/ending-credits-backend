package com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;
}
