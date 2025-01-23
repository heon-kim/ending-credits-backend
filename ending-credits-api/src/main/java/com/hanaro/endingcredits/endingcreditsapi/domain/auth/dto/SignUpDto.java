package com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto;

import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.LoginType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SignUpDto {
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;

    @NotBlank
    private LoginType loginType;

    @NotBlank
    private LocalDate birthDate;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String name;

    @NotBlank
    private String email;
}
