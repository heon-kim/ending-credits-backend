package com.hanaro.endingcredits.endingcreditsapi.domain.auth.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.MemberDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.OCRPort;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.InvalidJwtException;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.JwtHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.VerificationHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.mapper.MemberMapper;
import com.hanaro.endingcredits.endingcreditsapi.utils.security.JwtProvider;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final MemberMapper memberMapper;
    private final JwtProvider jwtProvider;
    private final OCRPort ocrPort;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Value("${coolsms.api-key}")
    private String coolsmsApiKey;

    @Value("${coolsms.api-secret}")
    private String coolsmsApiSecret;

    @Value("${coolsms.from-number}")
    private String coolsmsFromNumber;

    public String getKakaoClientId() {
        return kakaoClientId;
    }

    public String getKakaoRedirectUri() {
        return kakaoRedirectUri;
    }

    // 휴대폰 본인인증 코드 넣을 메모리
    private Map<String, CerificationCodeDto> cerificationCodes = new ConcurrentHashMap<>();

    private TokenPairResponseDto generateTokenPair(Map<String, ?> claims) {
        Date now = new Date();
        String accessToken = jwtProvider.generateAccessToken(claims, now);
        String refreshToken = jwtProvider.generateRefreshToken(claims, now);
        return TokenPairResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public LoginResponseDto generateTokenPairWithLoginDto(LoginDto loginDto) {
        MemberEntity member = memberRepository.findByIdentifier(loginDto.getIdentifier()).orElse(null);
        if (member == null) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        } else if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new MemberHandler(ErrorStatus.WRONG_PASSWORD);
        }
        Map<String, Object> memberClaims = Map.of("identifier", loginDto.getIdentifier(),
                "id", member.getMemberId());
        TokenPairResponseDto tokenPairResponseDto = generateTokenPair(memberClaims);

        return LoginResponseDto.builder()
                .name(member.getName())
                .tokenPair(tokenPairResponseDto)
                .build();
    }

    public LoginResponseDto generateTokenPairWithKaKaoLogin(String email) {
        MemberEntity member = memberRepository.findByEmail(email).orElse(null);

        if (member == null) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Map<String, Object> memberClaims = Map.of("identifier", member.getIdentifier(),
                "id", member.getMemberId());
        TokenPairResponseDto tokenPairResponseDto = generateTokenPair(memberClaims);

        return LoginResponseDto.builder()
                .name(member.getName())
                .tokenPair(tokenPairResponseDto)
                .build();
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
        memberRepository.findById(memberId).ifPresentOrElse(member -> {
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

    public void changePassword(String phoneNumber, String newPassword) {
        MemberEntity member = memberRepository.findByPhoneNumber(phoneNumber)
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

    public LoginResponseDto processKakaoLogin(String code) {
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

    public void sendSms(String phoneNumber) {
        try {
            Message messageService = new Message(coolsmsApiKey, coolsmsApiSecret);
            String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);

            long expiryTime = System.currentTimeMillis() + (5 * 60 * 1000); // 인증코드 5분 TTL
            cerificationCodes.put(phoneNumber, CerificationCodeDto.builder()
                            .code(certificationCode)
                            .expiryTime(expiryTime)
                            .build());

            HashMap<String, String> params = new HashMap<>();
            params.put("to", phoneNumber);
            params.put("from", coolsmsFromNumber);
            params.put("type", "SMS");
            params.put("text", "[엔딩크레딧] 인증번호 : " + certificationCode);
            params.put("app_version", "EndingCredits API 1.0");

            messageService.send(params);
        } catch (VerificationHandler | CoolsmsException e) {
            throw new VerificationHandler(ErrorStatus.VERIFICATION_CODE_SEND_FAILED);
        }
    }

    public void verifySms(String phoneNumber, String certificationCode) {
        CerificationCodeDto storedCode = cerificationCodes.get(phoneNumber);

        if (storedCode.getCode() == null || !storedCode.getCode().equals(certificationCode)) {
            throw new VerificationHandler(ErrorStatus.INVALID_VERIFICATION_CODE);
        } else if (storedCode.isExpired()) {
            throw new VerificationHandler(ErrorStatus.VERIFICATION_CODE_EXPIRED);
        }

        cerificationCodes.remove(phoneNumber);
    }

    @Scheduled(fixedRate = 300000) // 5분마다 만료된 인증코드 삭제 작업 실행
    public void removeExpiredCodes() {
        long currentTime = System.currentTimeMillis();
        cerificationCodes.entrySet().removeIf(entry -> entry.getValue().getExpiryTime() < currentTime);
    }

    public IdCardDto recognizeIdCard(MultipartFile file) {
        return ocrPort.recognizeIdCard(file);

    }
}
