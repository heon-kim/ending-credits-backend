package com.hanaro.endingcredits.endingcreditsapi.utils.mapper;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.SignUpForm;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberInfoDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source = "password", target = "password")
    @Mapping(target = "isActive", constant = "true")
    MemberEntity toMemberEntity(SignUpForm signUpForm, String password);

    MemberDto toMemberDto(MemberEntity memberEntity);

    MemberEntity toMemberEntity(MemberDto memberDto, UUID memberId);

    MemberInfoDto toMemberInfoDto(MemberEntity memberEntity);
}
