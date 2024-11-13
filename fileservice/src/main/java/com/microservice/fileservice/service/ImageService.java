package com.microservice.fileservice.service;

import com.microservice.fileservice.dto.request.DeleteRequest;
import com.microservice.fileservice.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageResponse uploadImage(MultipartFile[] images);
    String deleteImage(DeleteRequest request);
}
