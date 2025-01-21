package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClovaStorageProvider {

    private static final String BUCKET_NAME = "ending-credits";

    private final AmazonS3Client objectStorageClient;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        objectStorageClient.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return fileName;
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return UUID.randomUUID().toString() + extension;
    }
}
