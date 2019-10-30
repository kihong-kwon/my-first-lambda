package net.kkhstudy.myfirstlambda;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBTemplate;
import net.kkhstudy.myfirstlambda.dynamodb.query.DynamoDBEntityWithHashAndRangeKeyCriteria;
import net.kkhstudy.myfirstlambda.dynamodb.query.DynamoDBQueryCreator;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadata;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadataImpl;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBHashAndRangeKeyMetadata;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBHashAndRangeKeyMetadataImpl;
import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;
import net.kkhstudy.myfirstlambda.function.CreateEntityFunction;
import net.kkhstudy.myfirstlambda.function.GetEntityFunction;
import net.kkhstudy.myfirstlambda.function.S3DrivenFunction;
import net.kkhstudy.myfirstlambda.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class MyFirstLambdaApplication {

    @Autowired
    DynamoDBOperations dynamoDBOperations;

    @Autowired
    DynamoDBHashAndRangeKeyMetadata<DynamoDemoEntity> dynamoDBHashAndRangeKeyMetadata;

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
    public DynamoDBHashAndRangeKeyMetadata<DynamoDemoEntity> dynamoDBHashAndRangeKeyMetadata() {
        return new DynamoDBHashAndRangeKeyMetadataImpl<>(DynamoDemoEntity.class);
    }

    @Bean
    public TestRepository testRepository() {
        TestRepository testRepository = new TestRepository(dynamoDBHashAndRangeKeyMetadata, dynamoDBOperations);
        return testRepository;
    }

    @Bean
    public DynamoDBQueryCreator<DynamoDemoEntity> dynamoDBQueryCreator() {
        return new DynamoDBQueryCreator(dynamoDBHashAndRangeKeyMetadata, null, null, dynamoDBOperations);
    }

}
