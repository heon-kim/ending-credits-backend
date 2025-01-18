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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final MemberMapper memberMapper;
    private final JwtProvider jwtProvider;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    public String getKakaoClientId() {
        return kakaoClientId;
    }

    public String getKakaoRedirectUri() {
        return kakaoRedirectUri;
    }

    private TokenPairResponseDto generateTokenPair(Map<String, ?> claims) {
        Date now = new Date();
        String accessToken = jwtProvider.generateAccessToken(claims, now);
        String refreshToken = jwtProvider.generateRefreshToken(claims, now);
        return TokenPairResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenPairResponseDto generateTokenPairWithLoginDto(LoginDto loginDto) {
        MemberEntity member = memberRepository.findByIdentifier(loginDto.getIdentifier()).orElse(null);
        if (member == null) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        } else if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new MemberHandler(ErrorStatus.WRONG_PASSWORD);
        }
        Map<String, Object> memberClaims = Map.of("identifier", loginDto.getIdentifier(),
                "id", member.getMemberId());
        return generateTokenPair(memberClaims);
    }

    public TokenPairResponseDto generateTokenPairWithKaKaoLogin(String email) {
        MemberEntity member = memberRepository.findByEmail(email).orElse(null);

        if (member == null) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Map<String, Object> memberClaims = Map.of("identifier", member.getIdentifier(),
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

    public UUID signUp(SignUpDto signUpDto) {
        String identifier = signUpDto.getIdentifier();

        if (checkIdentifierExists(identifier)) {
            throw new MemberHandler(ErrorStatus.DUPLICATED_IDENTIFIER);
        }

        MemberEntity member = memberRepository.save(memberMapper.toMemberEntity(signUpDto, passwordEncoder.encode(signUpDto.getPassword())));

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

    public void checkIdentifier(String identifier) {
        boolean exists = checkIdentifierExists(identifier);

        if(exists) {
            throw new MemberHandler(ErrorStatus.DUPLICATED_IDENTIFIER);
        }
    }

    public void changePassword(UUID memberId, String newPassword) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.setPassword(passwordEncoder.encode(newPassword));

        memberRepository.save(member);
    }

    public void changeSimplePassword(String identifier, String newSimplePassword) {
        MemberEntity member = memberRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.setSimplePassword(newSimplePassword);

        memberRepository.save(member);
    }

    public TokenPairResponseDto processKakaoLogin(String code) {
        // 카카오 서버에서 Access Token 요청
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 토큰 요청
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                kakaoTokenUri,
                HttpMethod.POST,
                request,
                Map.class
        );
        String accessToken = tokenResponse.getBody().get("access_token").toString();

        // Access Token으로 사용자 정보 조회
        headers.set("Authorization", "Bearer " + accessToken);

        // 'property_keys' 파라미터 추가
        MultiValueMap<String, String> userInfoBody = new LinkedMultiValueMap<>();
        userInfoBody.add("property_keys", "[\"kakao_account.email\"]");

        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(userInfoBody, headers);

        // 사용자 정보 요청 (이메일)
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                kakaoUserInfoUri,
                HttpMethod.GET,
                userInfoRequest,
                Map.class
        );

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfoResponse.getBody().get("kakao_account");
        String email = kakaoAccount.get("email").toString();

        return generateTokenPairWithKaKaoLogin(email);
    }
}
