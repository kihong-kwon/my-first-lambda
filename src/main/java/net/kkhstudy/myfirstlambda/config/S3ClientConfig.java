package net.kkhstudy.myfirstlambda.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

@Profile("!local")
@Configuration
public class S3ClientConfig {

    @Value("${cloud.aws.s3.endpoint}")
    private String amazonAWSEndpoint;

    @Value("${cloud.aws.region.static}")
    private String amazonAWSRegion;

    @Bean
    public AmazonS3 awsS3Client(){
        if (!StringUtils.isEmpty(amazonAWSEndpoint))  {
            return AmazonS3ClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonAWSEndpoint, amazonAWSRegion)).build();
        } else {
            return AmazonS3ClientBuilder.standard().withRegion(amazonAWSRegion).build();
        }

    }
}
