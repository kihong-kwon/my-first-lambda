package net.kkhstudy.myfirstlambda.repository;

import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.repository.SimpleDynamoDBCrudRepository;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadata;
import net.kkhstudy.myfirstlambda.entity.TitleAuthorEntity;

public class TestRepository extends SimpleDynamoDBCrudRepository<TitleAuthorEntity> {
    public TestRepository(DynamoDBEntityMetadata<TitleAuthorEntity> dynamoDBEntityMetadata,
                          DynamoDBOperations dynamoDBOperations) {
        super(dynamoDBEntityMetadata, dynamoDBOperations);
    }
}
