package com.microservice.postservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.postservice.dto.ListPostResponse;
import com.microservice.postservice.dto.request.PostRequest;
import com.microservice.postservice.dto.response.PostResponse;
import com.microservice.postservice.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Validated
public class PostController {
    PostService postService;
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(@RequestBody @Valid PostRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PostResponse>builder()
                        .result(postService.crate(request))
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ListPostResponse<PostResponse>>> getAll(
            @Valid @Min(value = 1 , message = "INVALID_PAGE_MIN") @RequestParam(defaultValue = "1") int page,
            @Valid @Min(value = 1 , message = "INVALID_PAGE_MIN") @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok().body(ApiResponse.<ListPostResponse<PostResponse>>builder()
                        .result(postService.getPosts(page-1,size))
                .build());
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable String postId){
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPost(postId))
                .build();
    }

}
