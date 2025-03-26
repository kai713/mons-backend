package com.kairgaliyev.backendonlineshop.service.aws;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.UUID;

@Service
public class AwsS3Service {

    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${spring.aws.s3.access.key}")
    private String awsS3AccessKey;

    @Value("${spring.aws.s3.secret.key}")
    private String awsS3SecretKey;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsS3AccessKey, awsS3SecretKey)
                ))
                .build();
    }

    public String saveImageToS3(MultipartFile photo) {
        try {
            String s3FileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();

            // Создаем запрос на загрузку
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3FileName)
                    .contentType(photo.getContentType())
                    .build();

            // Загрузка файла в S3
            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(photo.getInputStream(), photo.getSize())
            );

            if (response.sdkHttpResponse().isSuccessful()) {
                return "https://" + bucketName + ".s3.eu-north-1.amazonaws.com/" + s3FileName;
            } else {
                throw new RuntimeException("Failed to upload image: " + response.sdkHttpResponse().statusText().orElse("Unknown error"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to upload image to S3 bucket", e);
        }
    }
}
