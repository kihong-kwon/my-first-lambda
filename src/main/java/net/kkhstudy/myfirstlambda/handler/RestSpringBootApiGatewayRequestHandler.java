package net.kkhstudy.myfirstlambda.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.springframework.cloud.function.adapter.aws.SpringBootApiGatewayRequestHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;


public class RestSpringBootApiGatewayRequestHandler extends SpringBootApiGatewayRequestHandler {
    @Override
    protected Object convertEvent(APIGatewayProxyRequestEvent event) {
        final Map<String, String> queryParams = event.getQueryStringParameters();

        return new GenericMessage<>(queryParams, getHeaders(event));
    }

    private MessageHeaders getHeaders(APIGatewayProxyRequestEvent event) {
        Map<String, Object> headers = new HashMap<>();
        if (event.getHeaders() != null) {
            headers.putAll(event.getHeaders());
        }
        headers.put("request", event);
        return new MessageHeaders(headers);
    }
}

