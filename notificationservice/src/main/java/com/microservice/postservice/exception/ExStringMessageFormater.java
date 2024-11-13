package com.microservice.postservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@Component
@Slf4j
public class ExStringMessageFormater {

    Map<Class<? extends RuntimeException>, BiFunction<RuntimeException,String, String>> strategyMap = new LinkedHashMap<>();

    public ExStringMessageFormater() {
        strategyMap.put(MethodArgumentTypeMismatchException.class, this::formatMethodArgumentTypeMismatchException);
    }



    public String format( RuntimeException ex,String message){

        var strategy = strategyMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAssignableFrom(ex.getClass()))
                .findFirst()
                .orElseThrow(() -> new NotificationException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        return strategy.getValue().apply(ex,message);
    }

    private String formatMethodArgumentTypeMismatchException(RuntimeException exception,String message){
        MethodArgumentTypeMismatchException ex = (MethodArgumentTypeMismatchException) exception;
        Map<String,String> mapString = new HashMap<>();
        mapString.put("fieldName", ex.getName());
        mapString.put("value", Objects.requireNonNull(ex.getValue()).toString());
        mapString.put("typeRequired", Objects.requireNonNull(ex.getRequiredType()).toString());


        return formatMessage(message, mapString);
    }

    private String formatMessage(String message,Map<String,String> replaceKeyValue) {
        for (Map.Entry<String, String> entry : replaceKeyValue.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            message = message.replace(placeholder, entry.getValue());
        }
        return message;
    }

}
