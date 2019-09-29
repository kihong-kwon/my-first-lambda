package net.kkhstudy.myfirstlambda;

import lombok.extern.slf4j.Slf4j;
import net.kkhstudy.myfirstlambda.Domain.PayloadRequest;
import net.kkhstudy.myfirstlambda.Domain.PayloadResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component("awsLambdaFunction")
public class AwsLambdaFunction implements Function<PayloadRequest, PayloadResponse> {

    @Override
    public PayloadResponse apply(PayloadRequest payloadRequest) {
        log.info("Retrieved response from another lambda: {}", payloadRequest.toString());
        PayloadResponse response = new PayloadResponse(payloadRequest.getMessage1(), payloadRequest.getMessage2());
        return response;
    }
}
