package com.microservice.postservice.mapper;

import com.microservice.postservice.dto.request.PostRequest;
import com.microservice.postservice.dto.response.PostResponse;
import com.microservice.postservice.entity.Image;
import com.microservice.postservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostRequest postRequest);
    PostResponse toPostResponse(Post post);
    @Mapping(target = "url", source = "urlImage")
    Image toImage(String urlImage);
    default List<Image> mapUrlsToImages(List<String> urlImages) {
        return urlImages.stream()
                .map(this::toImage)
                .toList();
    }
}
