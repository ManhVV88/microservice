package com.microservice.walletservice.service;

import com.microservice.walletservice.dto.ListPriceItemResponse;
import com.microservice.walletservice.dto.request.PriceItemRequest;
import com.microservice.walletservice.dto.response.PriceItemResponse;

public interface PriceItemService {
    PriceItemResponse createPriceForTypeItem(PriceItemRequest priceItemRequest);
    ListPriceItemResponse<PriceItemResponse> getAllPriceItems(int page, int size);
    PriceItemResponse getByItemType(String itemType);
    void deletePriceItem(String itemId);
}
