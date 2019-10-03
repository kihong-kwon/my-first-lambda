package net.kkhstudy.myfirstlambda.config;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;

@TestConfiguration
public class LocalStackSQSConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer LocalStackSQSConfig() {
        return new LocalStackContainer()
                .withServices(LocalStackContainer.Service.SQS);
    }

    @Bean
    public AmazonSQS amazonS3(LocalStackContainer localStackContainer) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
    }
}
