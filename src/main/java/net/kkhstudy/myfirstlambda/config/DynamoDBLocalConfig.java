package net.kkhstudy.myfirstlambda.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import net.kkhstudy.myfirstlambda.modules.localstack.EnableAwsLocalStack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

@Profile("local")
@EnableAwsLocalStack({DYNAMODB})
@Configuration
public class DynamoDBLocalConfig {
    @Bean
    public AmazonDynamoDB amazonDynamoDB(LocalStackContainer localStackContainer) {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(DYNAMODB))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
    }
}