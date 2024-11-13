package com.microservice.fileservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.fileservice.dto.request.DeleteRequest;
import com.microservice.fileservice.dto.response.ImageResponse;
import com.microservice.fileservice.service.ImageService;
import com.microservice.fileservice.validation.ValidateFileType;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Validated
public class ImageController {

    ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageResponse>> uploadImage(
            @RequestPart("image") @ValidateFileType(
                    allowedTypes = {"image/jpeg", "image/png", "image/gif"})
            MultipartFile[] images) {
        log.info("Uploading image");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ImageResponse>builder()
                        .result(imageService.uploadImage(images))
                        .build()
        );
    }
    @DeleteMapping()
    public ApiResponse<Void> deleteImage(@RequestBody DeleteRequest request) {
        var message = imageService.deleteImage(request);
        return ApiResponse.<Void>builder()
                .message(message)
                .build();
    }
}
