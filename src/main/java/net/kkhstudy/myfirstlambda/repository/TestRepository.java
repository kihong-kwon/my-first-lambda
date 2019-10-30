package net.kkhstudy.myfirstlambda.repository;

import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.repository.SimpleDynamoDBCrudRepository;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadata;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadataImpl;
import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;

public class TestRepository extends SimpleDynamoDBCrudRepository<DynamoDemoEntity> {
    public TestRepository(DynamoDBEntityMetadata<DynamoDemoEntity> dynamoDBEntityMetadata,
                          DynamoDBOperations dynamoDBOperations) {
        super(dynamoDBEntityMetadata, dynamoDBOperations);
    }
}
