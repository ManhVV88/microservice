package com.microservice.postservice.service;

import com.microservice.postservice.dto.ListPostResponse;
import com.microservice.postservice.dto.request.PostRequest;
import com.microservice.postservice.dto.response.PostResponse;


public interface PostService {
    PostResponse crate(PostRequest postRequest);
    ListPostResponse<PostResponse> getPosts(int page , int size);
    PostResponse getPost(String postId);
}
