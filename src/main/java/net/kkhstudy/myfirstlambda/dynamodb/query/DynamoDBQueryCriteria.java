package net.kkhstudy.myfirstlambda.dynamodb.query;

import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;

import java.util.Optional;

/**
 * @author Michael Lavelle
 * @author Sebastian Just
 */
public interface DynamoDBQueryCriteria<T> {

    DynamoDBQueryCriteria<T> withSingleValueCriteria(String propertyName, ComparisonOperator comparisonOperator,
                                                         Object value, Class<?> type);

    DynamoDBQueryCriteria<T> withNoValuedCriteria(String segment, ComparisonOperator null1);

    DynamoDBQueryCriteria<T> withPropertyEquals(String segment, Object next, Class<?> type);

    DynamoDBQueryCriteria<T> withPropertyIn(String segment, Iterable<?> o, Class<?> type);

    DynamoDBQueryCriteria<T> withPropertyBetween(String segment, Object value1, Object value2, Class<?> type);

    //DynamoDBQueryCriteria<T, ID> withSort(Sort sort);

    DynamoDBQueryCriteria<T> withProjection(Optional<String> projection);

    DynamoDBQueryCriteria<T> withLimit(Optional<Integer> limit);

    //Query<T> buildQuery(DynamoDBOperations dynamoDBOperations);

    //Query<Long> buildCountQuery(DynamoDBOperations dynamoDBOperations, boolean pageQuery);

}