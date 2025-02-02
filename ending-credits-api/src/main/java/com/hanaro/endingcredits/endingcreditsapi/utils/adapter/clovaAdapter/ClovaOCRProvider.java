package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.IdCardDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.id.ClovaDLResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.id.ClovaICResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.will.ClovaOCRWillFieldDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.will.ClovaOCRWillResponseDto;
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

    @Value("${clova.ocr.id.invoke-url}")
    private String idInvokeUrl;

    @Value("${clova.ocr.id.secret-key}")
    private String idSecretKey;

    @Value("${clova.ocr.will.invoke-url}")
    private String willInvokeUrl;

    @Value("${clova.ocr.will.secret-key}")
    private String willSecretKey;

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
        headers.set("X-OCR-SECRET", idSecretKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the form data
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("message", message);
        formData.add("file", file.getResource());

        // Create the HttpEntity
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(formData, headers);

        // Send the POST request
        String url = idInvokeUrl + "/document/id-card";
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

    public static String[] splitFileName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("Invalid file name: no extension found.");
        }
        String uuid = fileName.substring(0, lastDotIndex);
        String extension = fileName.substring(lastDotIndex + 1);
        return new String[]{uuid, extension};
    }

    public String recognizeText(String filePath) {
        String[] file = splitFileName(filePath);
        String uuid = file[0];
        String extension = file[1];
        String imageUrl = "https://kr.object.ncloudstorage.com/ending-credits/" + filePath;

        // Create the request body
        Map<String, Object> message = new HashMap<>();
        message.put("version", "V2");
        message.put("requestId", "UUID");
        message.put("timestamp", 0);
        Map<String, String> images = new HashMap<>();
        images.put("format", extension);
        images.put("name", uuid);
        images.put("url", imageUrl);
        message.put("images", new Map[]{images});

        // Create the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-OCR-SECRET", willSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(message, headers);

        // Send the POST request
        String url = willInvokeUrl + "/general";
        ResponseEntity<ClovaOCRWillResponseDto> response = restTemplate.postForEntity(url, entity, ClovaOCRWillResponseDto.class);

        // Parse the response and return the result
        StringBuilder result = new StringBuilder();
        if (response.getBody() != null) {
            for (ClovaOCRWillFieldDto field : response.getBody().getImages()[0].getFields()) {
                result.append(field.getInferText());
                if (field.isLineBreak()) {
                    result.append("\n");
                } else {
                    result.append(" ");
                }
            }
        }
        return result.toString().trim();
    }

    public String recognizeWill(List<String> filePaths) {
        StringBuilder will = new StringBuilder();
        for (String filePath : filePaths) {
            will.append(recognizeText(filePath));
        }
        return will.toString().trim();
    }
}
