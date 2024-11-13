package com.microservice.walletservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListPriceItemResponse<T>{

    long totalUser ;
    int totalPages;
    int currentPage;
    int size;

    @Builder.Default
    List<T> priceItems = Collections.emptyList();

}
