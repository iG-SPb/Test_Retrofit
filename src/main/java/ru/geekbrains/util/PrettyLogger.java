package ru.geekbrains.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import ru.geekbrains.dto.Product;

import java.util.List;

public class PrettyLogger implements HttpLoggingInterceptor.Logger {
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public void log(String message) {
        String trimmedMessage = message.trim();
        if (trimmedMessage.startsWith("{") && trimmedMessage.endsWith("}")) {
            try {
                Object value = mapper.readValue(message, Object.class);
                String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
                Platform.get().log(Platform.INFO, prettyJson, null);
            } catch (JsonProcessingException e) {
                Platform.get().log(Platform.WARN, message, e);
            }
        }
        else if (trimmedMessage.startsWith("[") && trimmedMessage.endsWith("]")) {
            try {
                Product[] productsList = new ObjectMapper().readValue(message, Product[].class);
                String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(productsList);
                Platform.get().log(Platform.INFO, prettyJson, null);
            } catch (JsonProcessingException e) {
                Platform.get().log(Platform.WARN, message, e);
            }
        }
        else {
            Platform.get().log(Platform.INFO, message, null);
        }
    }
}
