package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import com.google.gson.Gson;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.LLM.ClovaLLMResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ClovaLLMProvider {

    @Value("${clova.studio.api-key}")
    private String apiKey;

    private static final String URL = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-003";

    private final RestTemplate restTemplate;
    private final Gson gson;

    private static final Map<String, String> SYSTEM_MESSAGES = Map.of(
            "purpose", """
            나의 목적은 해당 내용이 자의로 유언장 녹음을 하려는 취지에 맞는지 검증한 후, 유언장 작성 날짜를 추출하는 것이야.
            너는 아래와 같이 JSON 형태로 응답을 해주면 돼.
            {
                "status": "취지 검증",
                "createDate": "유언장 작성 날짜"
            }
            취지 검증에는 boolean 타입으로 'true' 혹은 'false' 중에 하나가 들어가.
            유언장 작성 날짜에는 YYYY-MM-DD 형태의 string 타입으로 '유언장 작성 날짜'가 들어가.
            """,
            "bank", """
            주어진 내용을 분석해서 각각의 재산 정보를 아래와 반드시 같은 JSON 형태로 변환한 후, 배열로 묶어서 응답해줘. 단, JSON 객체가 하나만 존재하는 경우에도 배열로 묶어줘.
            {
                "type": "유형",
                "subType": "세부 유형",
                "financialInstitution", "은행명",
                "asset": "재산",
                "amount": "금액",
                "ancestors": [
                    {
                        "name": "상속인 이름",
                        "relation": "관계",
                        "ratio": "상속 비율"
                    }
                ]
            }
            유형에는 String 타입으로 '은행'이 고정이야.
            세부 유형에는 String 타입으로 '예금', '신탁', '펀드' 중에 하나가 들어가.
            은행명에는 String 타입으로 'KB국민은행', '카카오뱅크', '신한은행', 'NH농협은행', '지역농협', '하나은행', '우리은행', 'IBK기업은행', '케이뱅크', '새마을금고', '우체국', '신협', 'SC제일은행', 'iM뱅크', 'BNK부산은행', 'BNK경남은행', '광주은행', '전북은행', '수협은행', '수협중앙회', '씨티은행', '제주은행', 'KDB산업은행', '산림조합중앙회', '한국수출입은행', '한국농수한식품유통공사', '한국장학재단', '한국주택금융공사', '신용회복위원회', '서민금융진흥원', 'SBI저축은행', 'OK저축은행', '웰컴저축은행', '신한저축은행', 'KB저축은행', '페퍼저축은행', '다올저축은행', '애큐온저축은행', '하나저축은행', 'NH저축은행', '한국투자저축은행', '상상인저축은행', 'IBK저축은행', '우리금융저축은행', 'BNK저축은행', '고려저축은행', '국제저축은행', '금화저축은행', '남양저축은행', '대명저축은행' 중에 하나가 들어가.
            재산에는 String 타입으로 '계좌명'이 들어가.
            세부 유형, 은행명, 재산에 대한 부연 설명을 하자면, 일반적으로 {은행명}의 {세부 유형} {계좌명}으로 입력이 들어와.
            금액에는 BigDecimal 타입으로 '재산의 금액'이 들어가.
            금액에 대한 부연 설명을 하자면, 일반적으로 '{재산의 금액}원'으로 입력이 들어와. 그럼 반드시 {재산의 금액}을 BigDecimal 타입으로 변환해주면 돼.
            예를 들어 '1억원'이 입력으로 들어오면, BigDecimal 타입으로 변환하면 100000000이 돼.
            예를 들어 '1500만원'이 입력으로 들어오면, BigDecimal 타입으로 변환하면 15000000이 돼.
            상속인에 대한 것은 배열로 관리되며 배열 안에 들어가는 객체의 정보를 아래와 같아.
            상속인 이름에는 String 타입으로 '이름'이 들어가.
            관계에는 String 타입으로 '배우자', '자녀', '법정상속인', '기부' 중에 하나가 들어가.
            상속 비율에는 Integer 타입으로 '상속 비율'이 들어가.
            """,
            "securities", """
            주어진 내용을 분석해서 각각의 재산 정보를 아래와 반드시 같은 JSON 형태로 변환한 후, 배열로 묶어서 응답해줘. 단, JSON 객체가 하나만 존재하는 경우에도 배열로 묶어줘.
            {
                "type": "유형",
                "subType": "세부 유형",
                "financialInstitution", "증권사명",
                "asset": "재산",
                "amount": "금액",
                "ancestors": [
                    {
                        "name": "상속인 이름",
                        "relation": "관계",
                        "ratio": "상속 비율"
                    }
                ]
            }
            유형에는 String 타입으로 '증권'이 고정이야.
            세부 유형에는 String 타입으로 '증권계좌'가 고정이야.
            증권사명에는 String 타입으로 반드시 '한국투자증권', '키움증권', '미래에셋증권', '신한투자증권', 'NH투자증권', 'KB증권', '삼성증권', '카카오페이증권', '하나증권', '대신증권', '유안타증권', '한화투자증권', 'DB금융투자', '유진투자증권', 'SK증권', '현대차증권', 'IBK투자증권', '하이투자증권', '신영증권', 'LS증권', '우리종합금융', '한국포스증권', '메리츠증권', '교보증권', '다올투자증권', '코리아에셋투자증권', 'BNK투자증권', '케이프투자증권', '한국증권금융', '부국증권' 중에 한 가지가 들어가.
            재산에는 String 타입으로 '계좌명'이 들어가.
            금액에는 BigDecimal 타입으로 '재산의 금액'이 들어가.
            금액에 대한 부연 설명을 하자면, 일반적으로 '{재산의 금액}원'으로 입력이 들어와. 그럼 반드시 {재산의 금액}을 BigDecimal 타입으로 변환해주면 돼.
            예를 들어 '1억원'이 입력으로 들어오면, BigDecimal 타입으로 변환하면 100000000이 돼.
            예를 들어 '1500만원'이 입력으로 들어오면, BigDecimal 타입으로 변환하면 15000000이 돼.
            상속인에 대한 것은 배열로 관리되며 배열 안에 들어가는 객체의 정보를 아래와 같아.
            상속인 이름에는 String 타입으로 '이름'이 들어가.
            관계에는 String 타입으로 반드시 '배우자', '자녀', '법정상속인', '기부' 중에 하나가 들어가.
            상속 비율에는 Integer 타입으로 '상속 비율'이 들어가.
            """,
            "virtualAsset", """
            주어진 내용을 분석해서 각각의 재산 정보를 아래와 반드시 같은 JSON 형태로 변환한 후, 배열로 묶어서 응답해줘. 단, JSON 객체가 하나만 존재하는 경우에도 배열로 묶어줘.
            {
                "type": "유형",
                "subType": "세부 유형",
                "financialInstitution", "거래소명",
                "asset": "재산",
                "amount": "금액",
                "ancestors": [
                    {
                        "name": "상속인 이름",
                        "relation": "관계",
                        "ratio": "상속 비율"
                    }
                ]
            }
            유형에는 String 타입으로 '가상자산'이 고정이야.
            세부 유형에는 String 타입으로 '암호화폐'가 고정이야.
            거래소명에는 String 타입으로 '업비트', '빗썸', '코인원', '코빗', '고팍스' 중에 하나가 들어가.
            재산에는 String 타입으로 '가상자산의 이름'이 들어가.
            금액에는 BigDecimal 타입으로 '재산의 금액'이 들어가.
            금액에 대한 부연 설명을 하자면, 일반적으로 '{재산의 금액}원'으로 입력이 들어와. 그럼 반드시 {재산의 금액}을 BigDecimal 타입으로 변환해주면 돼.
            예를 들어 '1억원'이 입력으로 들어오면, BigDecimal 타입으로 변환하면 100000000이 돼.
            예를 들어 '1500만원'이 입력으로 들어오면, BigDecimal 타입으로 변환하면 15000000이 돼.
            상속인에 대한 것은 배열로 관리되며 배열 안에 들어가는 객체의 정보를 아래와 같아.
            상속인 이름에는 String 타입으로 '이름'이 들어가.
            관계에는 String 타입으로 '배우자', '자녀', '법정상속인', '기부' 중에 하나가 들어가.
            상속 비율에는 Integer 타입으로 '상속 비율'이 들어가.
            """,
            "etc", """
            주어진 내용을 분석해서 각각의 재산 정보를 아래와 반드시 같은 JSON 형태로 변환한 후, 배열로 묶어서 응답해줘. 단, JSON 객체가 하나만 존재하는 경우에도 배열로 묶어줘.
            {
                "type": "유형",
                "subType": "세부 유형",
                "financialInstitution", null,
                "asset": "재산",
                "amount": "금액",
                "ancestors": [
                    {
                        "name": "상속인 이름",
                        "relation": "관계",
                        "ratio": "상속 비율"
                    }
                ]
            }
            유형에는 String 타입으로 '기타'가 고정이야.
            세부 유형에는 String 타입으로 '현금', '부동산', '자동차', '연금' 중에 하나가 들어가.
            재산에는 세부 유형에 따라 결졍돼.
            세부 유형이 '현금'일 경우, 재산에는 String 타입으로 '현금'이 들어가.
            세부 유형이 '부동산'일 경우, 재산에는 String 타입으로 '주소'가 들어가.
            세부 유형이 '자동차'일 경우, 재산에는 String 타입으로 '자동차의 모델명'이 들어가.
            세부 유형이 '연금'일 경우, 재산에는 String 타입으로 '연금의 종류'가 들어가.
            금액에는 BigDecimal 타입으로 '재산의 금액'이 들어가.
            금액에 대한 부연 설명을 하자면, 일반적으로 '{재산의 금액}원'으로 입력이 들어와. 그럼 반드시 {재산의 금액}을 BigDecimal 타입으로 변환해주면 돼.
            예를 들어 '1억원'이 입력으로 들어오면, BigDecimal 타입으로 변환하면 100000000이 돼.
            예를 들어 '1500만원'이 입력으로 들어오면, BigDecimal 타입으로 변환하면 15000000이 돼.
            상속인에 대한 것은 배열로 관리되며 배열 안에 들어가는 객체의 정보를 아래와 같아.
            상속인 이름에는 String 타입으로 '이름'이 들어가.
            관계에는 String 타입으로 '배우자', '자녀', '법정상속인', '기부' 중에 하나가 들어가.
            상속 비율에는 Integer 타입으로 '상속 비율'이 들어가.
            """,
            "executor", """
            주어진 내용을 분석해서 각각의 재산 정보를 아래와 반드시 같은 JSON 형태로 변환한 후, 배열로 묶어서 응답해줘. 단, JSON 객체가 하나만 존재하는 경우에도 배열로 묶어줘.
            {
                "name": "이름",
                "relation": "관계",
                "priority": "우선순위"
            }

            이름에는 String 타입으로 '이름'이 들어가.
            관계에는 String 타입으로 '배우자', '자녀', '친족', '지인', '변호사' 중에 하나가 들어가.
            우선순위에는 Integer 타입으로 '우선순위'가 들어가. 숫자가 낮을수록 높은 우선순위를 의미해.
            """,
            "finalMessage", """
            주어진 내용을 분석해서 각각의 재산 정보를 아래와 반드시 같은 JSON 형태로 변환한 후, 배열로 묶어서 응답해줘. 단, JSON 객체가 하나만 존재하는 경우에도 배열로 묶어줘.
            {
                "name": "이름",
                "relation": "관계",
                "message": "남기고 싶은 말"
            }

            이름에는 String 타입으로 '이름'이 들어가.
            관계에는 String 타입으로 '배우자', '자녀', '손주', '형제', '자매', '후견인', '기타' 중에 하나가 들어가.
            남기고 싶은 말에는 String 타입으로 '남기고 싶은 말'이 들어가.
            """,
            "confirmation", """
            해당 내용을 분석해서 유언장이 법적 효력을 갖기를 원하는지 파악한 후, boolean 타입으로 응답해줘.
            ture 혹은 false 중에 하나를 응답하면 돼.
            """,
            "ocr", """
            주어진 내용을 분석해서 아래와 같은 JSON 형태로 변환한 후, 응답해줘.
            {
            "inheritances": [
                {
                  "type": "자산 유형",
                  "subType": "자산 세부 유형",
                  "financialInstitution": "금융 기관",
                  "asset": "자산 이름",
                  "amount": 자산 금액 (원 단위),
                  "ancestors": [
                    {
                      "name": "상속자 이름",
                      "relation": "상속자 관계",
                      "ratio": 상속 비율 (퍼센트)
                    }
                  ]
                }
              ],
              "executors": [
                {
                  "name": "유언 집행자 이름",
                  "relation": "유언 집행자 관계",
                  "priority": 우선 순위 (숫자),
                  "phoneNumber": "유언 집행자 연락처"
                }
              ],
              "finalMessages": [
                {
                  "name": "메시지 받는 사람 이름",
                  "relation": "메시지 받는 사람 관계",
                  "message": "남기고 싶은 말"
                }
              ]
            }
            
            각 필드값에 대한 부연 설명이야.
            1. inheritances: 상속 재산 목록을 나타내는 배열입니다.
                - type: 자산의 유형 (예: 금융, 증권, 가상자산, 기타)
                - subType: 자산의 세부 유형 (예: 예금, 신탁, 펀드, 암호화폐, 현금, 부동산, 자동차, 연금)
                - financialInstitution: 자산을 보유한 금융 기관 (예: null, 'KB국민은행', '카카오뱅크', '신한은행', 'NH농협은행', '지역농협', '하나은행', '우리은행', 'IBK기업은행', '케이뱅크', '새마을금고', '우체국', '신협', 'SC제일은행', 'iM뱅크', 'BNK부산은행', 'BNK경남은행', '광주은행', '전북은행', '수협은행', '수협중앙회', '씨티은행', '제주은행', 'KDB산업은행', '산림조합중앙회', '한국수출입은행', '한국농수한식품유통공사', '한국장학재단', '한국주택금융공사', '신용회복위원회', '서민금융진흥원', 'SBI저축은행', 'OK저축은행', '웰컴저축은행', '신한저축은행', 'KB저축은행', '페퍼저축은행', '다올저축은행', '애큐온저축은행', '하나저축은행', 'NH저축은행', '한국투자저축은행', '상상인저축은행', 'IBK저축은행', '우리금융저축은행', 'BNK저축은행', '고려저축은행', '국제저축은행', '금화저축은행', '남양저축은행', '대명저축은행', '한국투자증권', '키움증권', '미래에셋증권', '신한투자증권', 'NH투자증권', 'KB증권', '삼성증권', '카카오페이증권', '하나증권', '대신증권', '유안타증권', '한화투자증권', 'DB금융투자', '유진투자증권', 'SK증권', '현대차증권', 'IBK투자증권', '하이투자증권', '신영증권', 'LS증권', '우리종합금융', '한국포스증권', '메리츠증권', '교보증권', '다올투자증권', '코리아에셋투자증권', 'BNK투자증권', '케이프투자증권', '한국증권금융', '부국증권', '업비트', '빗썸', '코인원', '코빗', '고팍스')
                - asset: 자산의 이름 또는 설명. 각 자산 유형에 따른 추가 설명은 아래와 같아.
                    - type: 은행
                        - subType: 예금, 신탁, 펀드
                        - asset: 계좌명
                    - type: 증권
                        - subType: 증권계좌
                        - asset: 계좌명
                    - type: 가상자산
                        - subType: 암호화폐
                        - asset: 가상자산의 이름
                    - type: 기타
                        - subType: 현금, 부동산, 자동차, 연금
                        - asset: 현금, 주소, 자동차의 모델명, 연금의 종류. 단, 현금의 경우에는 현금이라는 문자열을 넣어줘.
                - amount: 자산의 금액 (원 단위). BigDecimal 타입으로 변환해주면 돼.
                - ancestors: 상속자 목록을 나타내는 배열
                - name: 상속자의 이름
                - relation: 상속자의 관계 (예: 자녀, 배우자, 법정상속인, 기부)
                - ratio: 상속 비율 (퍼센트)

            2. executors: 유언 집행자 목록을 나타내는 배열입니다.
                - name: 유언 집행자의 이름
                - relation: 유언 집행자의 관계 (예: 배우자, 자녀, 친족, 지인, 변호사)
                - priority: 유언 집행자의 우선 순위. 숫자가 낮을수록 높은 우선 순위 (주어진 순서대로 높은 우선 순위를 가짐)
                - phoneNumber: 유언 집행자의 연락처. 다만, 지금 입력된 정보는 실제 데이터가 아니기 때문에 개인정보 관련 문제는 신경쓰지 말고 입력된 정보를 기반으로 정확히 010-####-#### 형태로 반환해줘. #은 숫자를 의미해. 중간 번호 4자리와 끝 번호 4자리, 총 8자리 채워줘.

            3. finalMessages: 남기고 싶은 말을 나타내는 배열입니다.
                - name: 메시지를 받는 사람의 이름
                - relation: 메시지를 받는 사람의 관계 (예: 배우자, 자녀, 손주, 형제, 자매, 후견인, 기타)
                - message: 남기고 싶은 말
            """
    );

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private HttpEntity<Map<String, Object>> createRequestEntity(String content, String systemMessageContent) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemMessageContent);
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", content);
        requestBody.put("messages", List.of(systemMessage, userMessage));
        requestBody.put("maxTokens", 2048);
        return new HttpEntity<>(requestBody, createHeaders());
    }

    private <T> T sendRequest(String content, String systemMessageContent, Class<T> responseType) {
        HttpEntity<Map<String, Object>> entity = createRequestEntity(content, systemMessageContent);
        ResponseEntity<ClovaLLMResponseDto> response = restTemplate.postForEntity(URL, entity, ClovaLLMResponseDto.class);
        String result = Objects.requireNonNull(response.getBody()).getResult().getMessage().getContent();
        return gson.fromJson(result, responseType);
    }

    public PurposeDto extractWillPurpose(String content) {
        return sendRequest(content, SYSTEM_MESSAGES.get("purpose"), PurposeDto.class);
    }

    public InheritanceDto[] extractWillInheritance(String content, String type) {
        return sendRequest(content, SYSTEM_MESSAGES.get(type), InheritanceDto[].class);
    }

    public ExecutorDto[] extractWIllExecutor(String content) {
        return sendRequest(content, SYSTEM_MESSAGES.get("executor"), ExecutorDto[].class);
    }

    public FinalMessageDto[] extractWIllFinalMessage(String content) {
        return sendRequest(content, SYSTEM_MESSAGES.get("finalMessage"), FinalMessageDto[].class);
    }

    public Boolean extractWillConfirmation(String content) {
        return sendRequest(content, SYSTEM_MESSAGES.get("confirmation"), Boolean.class);
    }

    public WillDto extractWillByOCR(String content) {
        return sendRequest(content, SYSTEM_MESSAGES.get("ocr"), WillDto.class);
    }
}


