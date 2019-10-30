package net.kkhstudy.myfirstlambda;

import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.query.DynamoDBQueryCreator;
import net.kkhstudy.myfirstlambda.dynamodb.query.DynamoDBQueryCreatorImpl;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBHashAndRangeKeyMetadata;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBHashAndRangeKeyMetadataImpl;
import net.kkhstudy.myfirstlambda.entity.TitleAuthorEntity;
import net.kkhstudy.myfirstlambda.function.CreateEntityFunction;
import net.kkhstudy.myfirstlambda.function.GetEntityFunction;
import net.kkhstudy.myfirstlambda.function.S3DrivenFunction;
import net.kkhstudy.myfirstlambda.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyFirstLambdaApplication {

    @Autowired
    DynamoDBOperations dynamoDBOperations;

    @Autowired
    DynamoDBHashAndRangeKeyMetadata<TitleAuthorEntity> dynamoDBHashAndRangeKeyMetadata;

    public static void main(String[] args) {
        SpringApplication.run(MyFirstLambdaApplication.class, args);
    }

    @Bean
    public CreateEntityFunction createEntityFunction() {
        return new CreateEntityFunction();
    }

    @Bean
    public GetEntityFunction getEntityFunction() {
        return new GetEntityFunction();
    }

    @Bean
    public S3DrivenFunction s3DrivenFunction() {
        return new S3DrivenFunction();
    }

    @Bean
    public DynamoDBHashAndRangeKeyMetadata<TitleAuthorEntity> dynamoDBHashAndRangeKeyMetadata() {
        return new DynamoDBHashAndRangeKeyMetadataImpl<>(TitleAuthorEntity.class);
    }

    @Bean
    public TestRepository testRepository() {
        TestRepository testRepository = new TestRepository(dynamoDBHashAndRangeKeyMetadata, dynamoDBOperations);
        return testRepository;
    }

    @Bean
    public DynamoDBQueryCreator<TitleAuthorEntity> dynamoDBQueryCreator() {
        return new DynamoDBQueryCreatorImpl<>(dynamoDBHashAndRangeKeyMetadata, null, null, dynamoDBOperations);
    }

}
