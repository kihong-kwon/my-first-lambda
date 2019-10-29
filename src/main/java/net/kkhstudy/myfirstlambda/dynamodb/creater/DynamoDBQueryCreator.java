package net.kkhstudy.myfirstlambda.dynamodb.creater;

import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.socialsignin.spring.data.dynamodb.query.Query;
import org.socialsignin.spring.data.dynamodb.query.StaticQuery;
import org.socialsignin.spring.data.dynamodb.repository.query.AbstractDynamoDBQueryCreator;
import org.socialsignin.spring.data.dynamodb.repository.query.DynamoDBQueryCriteria;
import org.socialsignin.spring.data.dynamodb.repository.support.DynamoDBEntityInformation;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class DynamoDBQueryCreator<T> extends AbstractDynamoDBQueryCreator<T> {

    public DynamoDBQueryCreator(PartTree tree, ParameterAccessor parameterAccessor,
                                DynamoDBEntityInformation<T> entityMetadata, Optional<String> projection, Optional<Integer> limit,
                                DynamoDBOperations dynamoDBOperations) {
        super(tree, parameterAccessor, entityMetadata, projection, limit, dynamoDBOperations);
    }

    @Override
    protected Query<T> complete(@Nullable DynamoDBQueryCriteria<T> criteria, Sort sort) {
        if (criteria == null) {
            return new StaticQuery<T>(null);
        } else {
            criteria.withSort(sort);
            criteria.withProjection(projection);
            criteria.withLimit(limit);
            return criteria.buildQuery(dynamoDBOperations);
        }
    }

}
