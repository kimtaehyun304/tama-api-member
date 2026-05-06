package org.tama.tamaapi.feignClient.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tama.tamaapi.dto.responseDto.SimpleResponse;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public class ItemFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = readBody(response);
        String message = serializeBody(body);
        return new RuntimeException(message);
    }

    private String readBody(Response response){
        try {
            return Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        } catch (Exception e){
            throw new RuntimeException("메시지 바디 read 실패");
        }
    }

    private String serializeBody(String body){
        try {
            return objectMapper.readValue(body, SimpleResponse.class).getMessage();
        } catch (Exception e){
            throw new RuntimeException("메시지 바디 직렬화 실패");
        }
    }

}