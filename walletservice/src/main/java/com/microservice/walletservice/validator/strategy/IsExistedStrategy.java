package com.microservice.walletservice.validator.strategy;

import com.microservice.walletservice.constant.EntityValidated;
import com.microservice.walletservice.constant.Existed;
import com.microservice.walletservice.exception.ErrorCode;
import com.microservice.walletservice.exception.WalletException;
import com.microservice.walletservice.repository.PriceItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class IsExistedStrategy {

    PriceItemRepository priceItemRepository;


    Map<EntityValidated, BiFunction<Existed,String, Boolean>> strategyMap = new EnumMap<>(EntityValidated.class);

    {
        strategyMap.put(EntityValidated.PRICE_ITEM,this::isExistUser);
        strategyMap.put(EntityValidated.PACKAGE,this::isExistPackage);
    }

    public Boolean isExist(EntityValidated entityValidated, Existed existedTypeIsPassed, String id){

        var strategy = strategyMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(entityValidated))
                .findFirst()
                .orElseThrow(() -> new WalletException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        return strategy.getValue().apply(existedTypeIsPassed,id);
    }

    private boolean isExistUser(Existed existed,String itemType){
        return existed.equals(Existed.EXISTED_IS_PASSED) == priceItemRepository.existsByWalletItemType(itemType);
    }

    private boolean isExistPackage(Existed existed, String permission){
        return true;
    }


}
