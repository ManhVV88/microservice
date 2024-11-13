package com.microservice.walletservice.configuration;

import com.microservice.walletservice.constant.PriceForItem;
import com.microservice.walletservice.mapper.PriceItemMapper;
import com.microservice.walletservice.repository.PriceItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class InitPriceForItemInWalletConfiguration {
    PriceItemMapper priceItemMapper;
    @Bean
    ApplicationRunner initPriceForItemRunner(PriceItemRepository priceItemRepository) {
        return args -> {
            if(!priceItemRepository.existsByWalletItemType(PriceForItem.STAR.getWalletItemType())){
                priceItemRepository.save(priceItemMapper.toPrice(PriceForItem.STAR));
            }
            if(!priceItemRepository.existsByWalletItemType(PriceForItem.DIAMOND.getWalletItemType())){
                priceItemRepository.save(priceItemMapper.toPrice(PriceForItem.DIAMOND));
            }
            log.info("PriceForItemInWalletConfiguration initiated");
        };
    }
}
