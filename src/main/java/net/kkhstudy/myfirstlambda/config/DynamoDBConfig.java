package net.kkhstudy.myfirstlambda.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.kkhstudy.myfirstlambda.entity.IDemoEntity;
import net.kkhstudy.myfirstlambda.repositories.DynamoDemoEntityDeserializer;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

@Profile("!local")
@Configuration
@EnableDynamoDBRepositories(basePackages = "net.kkhstudy.myfirstlambda")
public class DynamoDBConfig {

    @Value("${cloud.aws.credentials.accessKey}")
    private String amazonAWSAccessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String amazonAWSSecretKey;

    @Value("${cloud.aws.dynamodb.endpoint}")
    private String amazonAWSEndpoint;

    @Value("${cloud.aws.region.static}")
    private String amazonAWSRegion;

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        if (!StringUtils.isEmpty(amazonAWSEndpoint))  {
            return AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonAWSEndpoint, amazonAWSRegion))
                    .build();
        } else {
            return AmazonDynamoDBClientBuilder.standard().withRegion(amazonAWSRegion).build();
        }
    }

    /*
    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig dynamoDBMapperConfig) {
        return new DynamoDBMapper(amazonDynamoDB, dynamoDBMapperConfig);
    }*/

    /*private AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }*/

    /*@Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }*/

    @Bean
    public Module dynamoDemoEntityDeserializer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(IDemoEntity.class, new DynamoDemoEntityDeserializer());
        return module;
    }
}
