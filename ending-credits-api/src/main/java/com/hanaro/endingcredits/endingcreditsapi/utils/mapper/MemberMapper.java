package com.hanaro.endingcredits.endingcreditsapi.utils.mapper;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.SignUpForm;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberInfoDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;

import java.util.UUID;

public class MemberMapper {
    public static MemberEntity toEntitySignUp(SignUpForm signUpForm, String password) {
        return MemberEntity.builder()
                .identifier(signUpForm.getIdentifier())
                .password(password)
                .simplePassword(signUpForm.getSimplePassword())
                .loginType(signUpForm.getLoginType())
                .birthDate(signUpForm.getBirthDate())
                .phoneNumber(signUpForm.getPhoneNumber())
                .address(signUpForm.getAddress())
                .name(signUpForm.getName())
                .isActive(true)
                .build();
    }

    public static MemberDto toDto(MemberEntity memberEntity) {
        return MemberDto.builder()
                .identifier(memberEntity.getIdentifier())
                .password(memberEntity.getPassword())
                .simplePassword(memberEntity.getSimplePassword())
                .loginType(memberEntity.getLoginType())
                .birthDate(memberEntity.getBirthDate())
                .phoneNumber(memberEntity.getPhoneNumber())
                .address(memberEntity.getAddress())
                .name(memberEntity.getName())
                .isActive(memberEntity.isActive())
                .build();
    }

    public static MemberEntity toEntity(MemberDto memberDto, UUID memberId) {
        return MemberEntity.builder()
                .id(memberId)
                .identifier(memberDto.getIdentifier())
                .password(memberDto.getPassword())
                .simplePassword(memberDto.getSimplePassword())
                .loginType(memberDto.getLoginType())
                .birthDate(memberDto.getBirthDate())
                .phoneNumber(memberDto.getPhoneNumber())
                .address(memberDto.getAddress())
                .name(memberDto.getName())
                .isActive(memberDto.isActive())
                .build();
    }

    public static MemberInfoDto toMemberInfoDto(MemberEntity memberEntity) {
        return MemberInfoDto.builder()
                .birthDate(memberEntity.getBirthDate())
                .phoneNumber(memberEntity.getPhoneNumber())
                .address(memberEntity.getAddress())
                .name(memberEntity.getName())
                .build();
    }
}
