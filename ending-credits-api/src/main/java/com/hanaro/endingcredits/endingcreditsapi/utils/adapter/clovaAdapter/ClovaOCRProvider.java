package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.IdCardDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.ClovaDLResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.ClovaICResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ClovaOCRProvider {

    @Value("${clova.ocr.invoke-url}")
    private String invokeUrl;

    @Value("${clova.ocr.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private String extractFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf('.') + 1);
    }

    public String extractPersonalNum(String personalNum) {
        String[] parts = personalNum.split("-");
        if (parts.length > 1) {
            return parts[0] + "-" + parts[1].charAt(0);
        }
        return personalNum;
    }

    public IdCardDto recognizeIdCard(MultipartFile file) {
        String fileExtension = extractFileExtension(file);

        // Create the request body
        Map<String, Object> message = new HashMap<>();
        message.put("version", "V2");
        message.put("requestId", "UUID");
        message.put("timestamp", 0);
        Map<String, String> image = new HashMap<>();
        image.put("format", fileExtension);
        image.put("name", "IdCard");
        message.put("images", new Map[]{image});

        // Create the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-OCR-SECRET", secretKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the form data
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("message", message);
        formData.add("file", file.getResource());

        // Create the HttpEntity
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(formData, headers);

        // Send the POST request
        String url = invokeUrl + "/document/id-card";
        ResponseEntity<Object> response = restTemplate.postForEntity(url, entity, Object.class);

        // Parse the response and map to IdCardDto
        if (response.getBody() != null) {
            Map<String, Object> responseBody = objectMapper.convertValue(response.getBody(), Map.class);
            String idType = (String) ((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) ((List<Object>) responseBody.get("images")).get(0)).get("idCard")).get("result")).get("idtype");
            if ("ID Card".equals(idType)) {
                ClovaICResponseDto clovaICResponseDto = objectMapper.convertValue(responseBody, ClovaICResponseDto.class);
                return new IdCardDto(
                        clovaICResponseDto.getImages().get(0).getIdCard().getResult().getIc().getName().get(0).getFormatted().getValue(),
                        clovaICResponseDto.getImages().get(0).getIdCard().getResult().getIc().getAddress().get(0).getFormatted().getValue(),
                        extractPersonalNum(clovaICResponseDto.getImages().get(0).getIdCard().getResult().getIc().getPersonalNum().get(0).getFormatted().getValue())
                );
            }
            else if ("Driver's License".equals(idType)) {
                ClovaDLResponseDto clovaDLResponseDto = objectMapper.convertValue(responseBody, ClovaDLResponseDto.class);
                return new IdCardDto(
                        clovaDLResponseDto.getImages().get(0).getIdCard().getResult().getDl().getName().get(0).getFormatted().getValue(),
                        clovaDLResponseDto.getImages().get(0).getIdCard().getResult().getDl().getAddress().get(0).getFormatted().getValue(),
                        extractPersonalNum(clovaDLResponseDto.getImages().get(0).getIdCard().getResult().getDl().getPersonalNum().get(0).getFormatted().getValue())
                );

            }
        }

        return null;
    }
}
