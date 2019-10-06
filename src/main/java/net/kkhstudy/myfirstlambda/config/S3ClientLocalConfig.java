package net.kkhstudy.myfirstlambda.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import net.kkhstudy.myfirstlambda.modules.localstack.EnableAwsLocalStack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Profile("local")
@EnableAwsLocalStack({S3})
@Configuration
public class S3ClientLocalConfig {
    @Bean
    public AmazonS3 awsS3Client(LocalStackContainer localStackContainer) {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
    }
}
