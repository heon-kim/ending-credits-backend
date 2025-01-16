package com.hanaro.endingcredits.endingcreditsapi.domain.auth.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.InvalidJwtException;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.JwtHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.mapper.MemberMapper;
import com.hanaro.endingcredits.endingcreditsapi.utils.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final JwtProvider jwtProvider;

    private TokenPairResponseDto generateTokenPair(Map<String, ?> claims) {
        Date now = new Date();
        String accessToken = jwtProvider.generateAccessToken(claims, now);
        String refreshToken = jwtProvider.generateRefreshToken(claims, now);
        return TokenPairResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenPairResponseDto generateTokenPairWithLoginForm(LoginForm loginForm) {
        MemberEntity member = memberRepository.findByIdentifier(loginForm.getIdentifier()).orElse(null);
        if (member == null) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        } else if (!passwordEncoder.matches(loginForm.getPassword(), member.getPassword())) {
            throw new MemberHandler(ErrorStatus.WRONG_PASSWORD);
        }
        Map<String, Object> memberClaims = Map.of("identifier", loginForm.getIdentifier(),
                "id", member.getMemberId());
        return generateTokenPair(memberClaims);
    }

    public TokenPairResponseDto refreshTokenPair(String oldRefreshToken) {
        try {
            Map<String, ?> claims = jwtProvider.parseRefreshToken(oldRefreshToken);
            return generateTokenPair(claims);
        } catch (InvalidJwtException e) {
            throw new JwtHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

    public UUID signUp(SignUpForm signUpForm) {
        String identifier = signUpForm.getIdentifier();

        if (checkIdentifierExists(identifier)) {
            throw new MemberHandler(ErrorStatus.DUPLICATED_IDENTIFIER);
        }

        MemberEntity member = memberRepository.save(memberMapper.toMemberEntity(signUpForm, passwordEncoder.encode(signUpForm.getPassword())));

        return member.getMemberId();
    }

    public void unsubscribe(UUID memberId) {
        memberRepository.findByMemberId(memberId).ifPresentOrElse(member -> {
            MemberDto memberDto = memberMapper.toMemberDto(member);
            memberDto.setActive(false);
            memberRepository.save(memberMapper.toMemberEntity(memberDto, memberId));
        }, () -> {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        });
    }

    public boolean checkIdentifierExists(String identifier) {
        return memberRepository.findByIdentifier(identifier).isPresent();
    }

    public void checkIdentifier(IdentifierForm identifierForm) {
        boolean exists = checkIdentifierExists(identifierForm.getIdentifier());

        if(exists) {
            throw new MemberHandler(ErrorStatus.DUPLICATED_IDENTIFIER);
        }
    }
}
