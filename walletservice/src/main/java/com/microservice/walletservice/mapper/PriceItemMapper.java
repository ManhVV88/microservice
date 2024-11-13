package com.microservice.walletservice.mapper;

import com.microservice.walletservice.constant.PriceForItem;
import com.microservice.walletservice.dto.request.PriceItemRequest;
import com.microservice.walletservice.dto.response.PriceItemResponse;
import com.microservice.walletservice.entity.PriceItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceItemMapper {
    PriceItem toPrice(PriceForItem priceForItem);
    PriceItem toPrice(PriceItemRequest priceItemRequest);
    PriceItemResponse toPriceResponse(PriceItem priceItem);
}
