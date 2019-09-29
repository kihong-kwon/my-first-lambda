package net.kkhstudy.myfirstlambda;

import lombok.extern.slf4j.Slf4j;
import net.kkhstudy.myfirstlambda.Domain.PayloadRequest;
import net.kkhstudy.myfirstlambda.Domain.PayloadResponse;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

@Slf4j
public class AwsLambdaFunctionHandler extends SpringBootRequestHandler<PayloadRequest, PayloadResponse> {
}
