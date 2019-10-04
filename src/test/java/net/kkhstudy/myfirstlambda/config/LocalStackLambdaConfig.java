package net.kkhstudy.myfirstlambda.config;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;

@TestConfiguration
public class LocalStackLambdaConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer()
                .withServices(LocalStackContainer.Service.LAMBDA);
    }

    @Bean
    public AWSLambda awsLambda(LocalStackContainer localStackContainer) {
        return AWSLambdaClientBuilder.standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
    }
}
