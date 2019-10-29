package net.kkhstudy.myfirstlambda.dynamodb.repository;

import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadataImpl;

public class SimpleDynamoDBPagingAndSortingRepository <T> extends SimpleDynamoDBCrudRepository<T> {

    public SimpleDynamoDBPagingAndSortingRepository(DynamoDBEntityMetadataImpl<T> dynamoDBEntityMetadata,
                                 DynamoDBOperations dynamoDBOperations) {
        super(dynamoDBEntityMetadata, dynamoDBOperations);
    }

    
}
