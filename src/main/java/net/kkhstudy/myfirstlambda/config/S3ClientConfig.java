package net.kkhstudy.myfirstlambda.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!local")
@Configuration
public class S3ClientConfig {
    @Bean
    public AmazonS3 awsS3Client(){
        return AmazonS3ClientBuilder.standard().build();
    }
}
