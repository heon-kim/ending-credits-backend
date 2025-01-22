package com.hanaro.endingcredits.endingcreditsapi.domain.member.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberInfoDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberUpdateInfoDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberInfoDto getMemberInfo(UUID memberId) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return memberMapper.toMemberInfoDto(member);
    }

    public void setMemberInfo(UUID memberId, MemberUpdateInfoDto memberUpdateInfoDto) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 변경 요청된 값만 변경
        if (memberUpdateInfoDto.getPhoneNumber() != null && !memberUpdateInfoDto.getPhoneNumber().isEmpty()) {
            member.setPhoneNumber(memberUpdateInfoDto.getPhoneNumber());
        }

        if (memberUpdateInfoDto.getAddress() != null && !memberUpdateInfoDto.getAddress().isEmpty()) {
            member.setAddress(memberUpdateInfoDto.getAddress());
        }

        memberRepository.save(member);
    }

    public void setWishFund(UUID memberId, Long wishFund) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.setWishFund(wishFund);
        memberRepository.save(member);
    }

    public String getWishFund(UUID memberId) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        String wishFund = formatter.format(member.getWishFund());

        return wishFund;
    }
}
