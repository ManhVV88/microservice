package com.microservice.fileservice.service.Impl;

import com.microservice.fileservice.dto.request.DeleteRequest;
import com.microservice.fileservice.dto.response.ImageResponse;
import com.microservice.fileservice.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {
    S3Uploader s3Uploader = new S3Uploader("bucket-image-socialbook");
    @Override
    public ImageResponse uploadImage(MultipartFile[] images) {
        return ImageResponse.builder()
                .url(s3Uploader.uploadImage(images))
                .build();
    }

    @Override
    public String deleteImage(DeleteRequest request) {
        return s3Uploader.deleteImage(request);
    }
}
