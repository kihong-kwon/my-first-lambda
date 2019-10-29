package net.kkhstudy.myfirstlambda.dynamodb.query;

import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadataImpl;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public class DynamoDBQueryCreator<T> extends AbstractDynamoDBQueryCreator<T> {

    public DynamoDBQueryCreator(DynamoDBEntityMetadataImpl<T> entityMetadata,
                                Optional<String> projection, Optional<Integer> limitResults, DynamoDBOperations dynamoDBOperations) {
        super(entityMetadata, projection, limitResults, dynamoDBOperations);
    }

    protected List<T> complete(@Nullable DynamoDBQueryCriteria<T> criteria, Sort sort) {
        criteria.withSort(sort);
        criteria.withProjection(projection);
        criteria.withLimit(limit);
        return criteria.buildQuery(dynamoDBOperations);
    }

}
