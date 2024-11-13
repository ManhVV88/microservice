package com.microservice.walletservice.service.impl;

import com.microservice.walletservice.dto.ListPriceItemResponse;
import com.microservice.walletservice.dto.request.PriceItemRequest;
import com.microservice.walletservice.dto.response.PriceItemResponse;
import com.microservice.walletservice.entity.PriceItem;
import com.microservice.walletservice.exception.ErrorCode;
import com.microservice.walletservice.exception.WalletException;
import com.microservice.walletservice.mapper.PriceItemMapper;
import com.microservice.walletservice.repository.PriceItemRepository;
import com.microservice.walletservice.service.PriceItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PriceItemServiceImpl implements PriceItemService {
    PriceItemRepository priceItemRepository;
    PriceItemMapper priceItemMapper;
    @Override
    public PriceItemResponse createPriceForTypeItem(PriceItemRequest priceItemRequest) {
        PriceItem priceItem = priceItemMapper.toPrice(priceItemRequest);
        return priceItemMapper.toPriceResponse(priceItemRepository.save(priceItem));
    }

    @Override
    public ListPriceItemResponse<PriceItemResponse> getAllPriceItems(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        var priceItems = priceItemRepository.findAll(pageable);

        return ListPriceItemResponse.<PriceItemResponse>builder()
                .currentPage(page+1)
                .totalPages(priceItems.getTotalPages())
                .totalUser(priceItems.getTotalElements())
                .size(priceItems.getSize())
                .priceItems(priceItems.getContent().stream().map(priceItemMapper::toPriceResponse).toList())
                .build();
    }

    @Override
    public PriceItemResponse getByItemType(String itemType) {
        return priceItemMapper.toPriceResponse(priceItemRepository.findByWalletItemType(itemType)
                .orElseThrow(() -> new WalletException(ErrorCode.INVALID_TYPE_ITEM_NOT_FOUND)));
    }

    @Override
    public void deletePriceItem(String itemType) {
        priceItemRepository.deleteByWalletItemType(itemType);
    }
}
