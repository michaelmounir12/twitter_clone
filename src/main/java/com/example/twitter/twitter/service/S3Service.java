package com.example.twitter.twitter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client ;
    private final String bucketName;
    private final Region awsRegion; // Store region here

    public S3Service(@Value("${aws.accessKeyId}") String accessKeyId,
                     @Value("${aws.secretAccessKey}") String secretAccessKey,
                     @Value("${aws.region}") String region,
                     @Value("${aws.s3.bucket}") String bucketName) {

        this.awsRegion = Region.of(region); // Parse it once

        this.s3Client = S3Client.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();

        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Save the file temporarily before uploading
        Path tempFile = Files.createTempFile("upload-", Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(tempFile.toFile());

        s3Client.putObject(request, tempFile);

        // Delete temp file after upload
        Files.deleteIfExists(tempFile);

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, awsRegion.id(), key);
    }
}
