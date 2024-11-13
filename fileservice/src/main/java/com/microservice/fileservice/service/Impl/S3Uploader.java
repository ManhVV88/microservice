package com.microservice.fileservice.service.Impl;
import com.microservice.fileservice.dto.request.DeleteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class S3Uploader {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Uploader(String bucketName) {
        this.bucketName = bucketName;
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create("social_key"))
                .build();
    }

    public List<String> uploadImage(MultipartFile[] files) {
        List<String> result = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                var fileName = file.getOriginalFilename();
                var key = STR."\{SecurityContextHolder.getContext().getAuthentication().getName()}/\{fileName}";

                Path tempFile = Files.createTempFile("temp-", fileName);
                Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                s3Client.putObject(putObjectRequest, tempFile);
                Files.delete(tempFile);

                result.add(STR."https://\{bucketName}.s3.\{Region.AP_SOUTHEAST_1}.amazonaws.com/\{key}");
            }

            return result;
        } catch (S3Exception | IOException e) {
            log.error("Error while uploading image : {}", e.getMessage());
            return null;
        }
    }
    public String deleteImage(DeleteRequest request) {
        var userFolder = SecurityContextHolder.getContext().getAuthentication().getName();
        return request.getListImageId().stream()
                .map(image -> {
                    var key = STR."\{userFolder}/\{image}";
                    try {

                        HeadObjectRequest headRequest = HeadObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .build();
                        //kiểm tra tồn tại và quyền truy cập
                        HeadObjectResponse headResponse = s3Client.headObject(headRequest);

                        if (headResponse != null) {
                            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(key)
                                    .build();
                            s3Client.deleteObject(deleteObjectRequest);
                        }
                        return "success";
                    } catch (S3Exception e) {
                        log.error("delete fail : {}", e.getMessage());
                        return  "fail";
                    }
                })
                .filter("fail"::equals)
                .findFirst()
                .orElse("success");
    }
}
