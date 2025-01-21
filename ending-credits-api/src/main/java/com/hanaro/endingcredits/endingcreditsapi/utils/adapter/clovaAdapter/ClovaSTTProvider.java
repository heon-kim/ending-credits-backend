package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ClovaSTTProvider {

    @Value("${clova.speech.secret-key}")
    private String secretKey;

    @Value("${clova.speech.invoke-url}")
    private String invokeUrl;

    private final RestTemplate restTemplate;

    public String transferSpeechToText(String filePath) {
        // Create the request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("dataKey", filePath);
        requestBody.put("language", "ko-KR");
        requestBody.put("completion", "sync");

        // Create the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-CLOVASPEECH-API-KEY", secretKey);

        // Create the HttpEntity
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Send the POST request
        String url = invokeUrl + "/recognizer/object-storage";
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }
}
