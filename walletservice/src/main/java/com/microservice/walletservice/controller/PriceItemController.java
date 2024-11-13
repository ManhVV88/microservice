package com.microservice.walletservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.walletservice.dto.ListPriceItemResponse;
import com.microservice.walletservice.dto.request.PriceItemRequest;
import com.microservice.walletservice.dto.response.PriceItemResponse;
import com.microservice.walletservice.service.PriceItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/price")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class PriceItemController {
    PriceItemService priceItemService;
    @PostMapping
    public ResponseEntity<ApiResponse<PriceItemResponse>> create(@RequestBody @Valid PriceItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PriceItemResponse>builder()
                        .result(priceItemService.createPriceForTypeItem(request))
                        .build());
    }

    @GetMapping
    public ApiResponse<ListPriceItemResponse<PriceItemResponse>> getAll(
            @Valid @Min(value = 1 , message = "INVALID_PAGE_MIN") @RequestParam(defaultValue = "1") int page,
            @Valid @Min(value = 1 , message = "INVALID_PAGE_MIN") @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<ListPriceItemResponse<PriceItemResponse>>builder()
                .result(priceItemService.getAllPriceItems(page-1, size))
                .build();
    }


    @GetMapping("/{itemType}")
    public ApiResponse<PriceItemResponse> getByItemType(@PathVariable String itemType) {
        return ApiResponse.<PriceItemResponse>builder()
                .result(priceItemService.getByItemType(itemType))
                .build();
    }

    @DeleteMapping("/{itemType}")
    public ApiResponse<Void> deleteByItemType(@PathVariable String itemType) {
        priceItemService.deletePriceItem(itemType);
        return ApiResponse.<Void>builder()
                .message("delete successful")
                .build();
    }
}
