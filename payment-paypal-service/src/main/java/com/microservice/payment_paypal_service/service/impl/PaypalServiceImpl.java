package com.microservice.payment_paypal_service.service.impl;

import com.microservice.commonbase.event.dto.NotificationEvent;
import com.microservice.payment_paypal_service.dto.request.PaymentRequest;
import com.microservice.payment_paypal_service.dto.response.PaymentPaypalResponse;
import com.microservice.payment_paypal_service.dto.request.PaymentSuccessRequest;
import com.microservice.payment_paypal_service.dto.response.PaymentSucessResponse;
import com.microservice.payment_paypal_service.exception.ErrorCode;
import com.microservice.payment_paypal_service.exception.PaypalException;
import com.microservice.payment_paypal_service.repository.httpClient.PaymentClient;
import com.microservice.payment_paypal_service.repository.httpClient.WalletClient;
import com.microservice.payment_paypal_service.service.PaypalService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaypalServiceImpl implements PaypalService {
    WalletClient walletClient;
    PayPalHttpClient payPalHttpClient;
    PaymentClient paymentClient;

    KafkaTemplate<String, Object> kafkaTemplate;


    @NonFinal
    @Value("${paypal.redirectUrl}/success")
    String returnUrl;
    @NonFinal
    @Value("${paypal.redirectUrl}/cancel")
    String cancelUrl;

    @Override
    public PaymentPaypalResponse createPayment(PaymentRequest request) {
        var rechargeResponse = walletClient.getRechargeId(request.getRechargeHistoryId()).getResult();
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        BigDecimal totalPrice = BigDecimal.valueOf((rechargeResponse.getAmount()*rechargeResponse.getQuantity())/1000L);
        AmountWithBreakdown amountWithBreakdown = new AmountWithBreakdown().currencyCode("USD")
                .value(totalPrice.toString());
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().amountWithBreakdown(amountWithBreakdown);
        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(STR."\{returnUrl}?rechargeId=\{rechargeResponse.getRechargeHistoryId()}")
                .cancelUrl(cancelUrl)
                .brandName("Social Book")
                .landingPage("BILLING")
                .userAction("PAY_NOW")
                .shippingPreference("NO_SHIPPING");

        orderRequest.applicationContext(applicationContext);
        OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            HttpResponse<Order> orderHttpResponse = payPalHttpClient.execute(ordersCreateRequest);
            Order order = orderHttpResponse.result();
            String redirectUrl = order.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .findFirst()
                    .orElseThrow(() -> new PaypalException(ErrorCode.UNCATEGORIZED_EXCEPTION))
                    .href();

            return new PaymentPaypalResponse("success", order.id(), redirectUrl);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new PaypalException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public PaymentSucessResponse successPayment(String rechargeId, String token) {
        var rechargeResponse = walletClient.getRechargeId(rechargeId).getResult();
        OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(token);
        try {
            HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
            if (httpResponse.result().status() != null) {
                Order order = httpResponse.result();
                Capture capture = order.purchaseUnits().getFirst().payments().captures().getFirst();
                var price = rechargeResponse.getAmount();
                var quantity = rechargeResponse.getQuantity();
                var itemType = rechargeResponse.getWalletItemType();


                BigDecimal amount = new BigDecimal(capture.amount().value());
                BigDecimal totalPrice = BigDecimal.valueOf((price*quantity)/1000L);
                if(amount.compareTo(totalPrice) != 0) {
                    log.error("amountPayPal != priceInRechargeHistory : {} != {}",amount,totalPrice );
                    throw new PaypalException(ErrorCode.UNCATEGORIZED_EXCEPTION);
                }

                String paypalFee = capture.sellerReceivableBreakdown().paypalFee().value();
                BigDecimal paymentFee = new BigDecimal(paypalFee);
                var nameEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                kafkaTemplate.send("notification-event", NotificationEvent.builder()
                        .channel("EMAIL")
                        .recipient(nameEmail)
                        .subject("Payment point success")
                        .body(STR."Thanks \{nameEmail} ! you are has pay success amount : \{amount} for "
                            + STR."\{quantity} \{itemType} . If your wallet has not received points, "
                            + "please contact us for support via phone number 0988892157. "
                        )
                        .build());

                PaymentSuccessRequest successPayment = PaymentSuccessRequest.builder()
                        .paymentFee(paymentFee)
                        .gatewayTransactionId(order.id())
                        .amount(amount)
                        .paymentStatus(order.status())
                        .paymentMethod("PAYPAL")
                        .quantity(quantity)
                        .itemType(itemType)
                        .rechargeId(rechargeId)
                        .build();

                var walletResponse = paymentClient.paymentSuccess(successPayment);

                return PaymentSucessResponse.builder()
                        .walletResponse(walletResponse.getResult())
                        .paymentSuccessRequest(successPayment)
                        .build();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new PaypalException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        throw new PaypalException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
