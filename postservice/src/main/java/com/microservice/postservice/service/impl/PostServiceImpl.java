package com.microservice.postservice.service.impl;

import com.microservice.postservice.dto.ListPostResponse;
import com.microservice.postservice.dto.request.PostRequest;
import com.microservice.postservice.dto.response.PostResponse;
import com.microservice.postservice.entity.Image;
import com.microservice.postservice.entity.Post;
import com.microservice.postservice.exception.ErrorCode;
import com.microservice.postservice.exception.PostException;
import com.microservice.postservice.mapper.PostMapper;
import com.microservice.postservice.repository.PostRepository;
import com.microservice.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;

    @Override
    public PostResponse crate(PostRequest postRequest) {
        Post post =  postMapper.toPost(postRequest);
        post.setAuthorEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Image> images = postMapper.mapUrlsToImages(postRequest.getUrlImages());
        post.setListImages(images);
        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public ListPostResponse<PostResponse> getPosts(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        var posts = postRepository.findAll(pageable);
        return ListPostResponse.<PostResponse>builder()
                .totalPost(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .currentPage(page+1)
                .size(posts.getSize())
                .posts(posts.getContent().stream().map(postMapper::toPostResponse).toList())
                .build();
    }

    @Override
    public PostResponse getPost(String postId) {

        return postMapper.toPostResponse(postRepository.findById(postId).orElseThrow(
                () -> new PostException(ErrorCode.INVALID_NOT_FOUND)
        ));
    }
}
