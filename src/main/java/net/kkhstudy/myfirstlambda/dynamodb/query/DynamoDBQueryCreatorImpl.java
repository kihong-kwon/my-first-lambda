package net.kkhstudy.myfirstlambda.dynamodb.query;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadata;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadataImpl;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBHashAndRangeKeyMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DynamoDBQueryCreatorImpl<T> implements DynamoDBQueryCreator<T> {

    protected final DynamoDBEntityMetadata<T> entityMetadata;
    protected final DynamoDBOperations dynamoDBOperations;
    protected final Optional<String> projection;
    protected final Optional<Integer> limit;
    protected DynamoDBQueryCriteria<T> criteria;

    public DynamoDBQueryCreatorImpl(DynamoDBEntityMetadata<T> entityMetadata,
                                        Optional<String> projection, Optional<Integer> limitResults, DynamoDBOperations dynamoDBOperations) {
        //super(tree);
        this.entityMetadata = entityMetadata;
        this.projection = projection;
        this.limit = limitResults;
        this.dynamoDBOperations = dynamoDBOperations;
    }

    @Override
    public DynamoDBQueryCreator<T> addCriteria(Class<?> propertyType, String operation, String propertiName, Iterator<Object> iterator) {
        final DynamoDBMapperTableModel<T> tableModel = dynamoDBOperations.getTableModel(entityMetadata.getJavaType());
        criteria = entityMetadata.isRangeKeyAware()
                ? new DynamoDBEntityWithHashAndRangeKeyCriteria<T>(
                (DynamoDBHashAndRangeKeyMetadata<T>) entityMetadata, tableModel)
                : new DynamoDBEntityWithHashKeyOnlyCriteria<T>(entityMetadata, tableModel);

        switch (operation) {
            case "IN" :
                Object in = iterator.next();
                Assert.notNull(in, "Creating conditions on null parameters not supported: please specify a value for '"
                        + propertyType + "'");
                boolean isIterable = ClassUtils.isAssignable(Iterable.class, in.getClass());
                boolean isArray = ObjectUtils.isArray(in);
                Assert.isTrue(isIterable || isArray, "In criteria can only operate with Iterable or Array parameters");
                Iterable<?> iterable = isIterable ? ((Iterable<?>) in) : Arrays.asList(ObjectUtils.toObjectArray(in));
                criteria.withPropertyIn(propertiName, iterable, propertyType);
                return this;
            case "CONTAINING" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.CONTAINS,
                        iterator.next(), propertyType);
                return this;
            case "STARTING_WITH" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.BEGINS_WITH,
                        iterator.next(), propertyType);
                return this;
            case "BETWEEN" :
                Object first = iterator.next();
                Object second = iterator.next();
                criteria.withPropertyBetween(propertiName, first, second, propertyType);
                return this;
            case "AFTER" :
            case "GREATER_THAN" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.GT, iterator.next(),
                        propertyType);
                return this;
            case "BEFORE" :
            case "LESS_THAN" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.LT, iterator.next(),
                        propertyType);
                return this;
            case "GREATER_THAN_EQUAL" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.GE, iterator.next(),
                        propertyType);
                return this;
            case "LESS_THAN_EQUAL" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.LE, iterator.next(),
                        propertyType);
                return this;
            case "IS_NULL" :
                criteria.withNoValuedCriteria(propertiName, ComparisonOperator.NULL);
                return this;
            case "IS_NOT_NULL" :
                criteria.withNoValuedCriteria(propertiName, ComparisonOperator.NOT_NULL);
                return this;
            case "TRUE" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.EQ, Boolean.TRUE,
                        propertyType);
                return this;
            case "FALSE" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.EQ, Boolean.FALSE,
                        propertyType);
                return this;
            case "SIMPLE_PROPERTY" :
                criteria.withPropertyEquals(propertiName, iterator.next(), propertyType);
                return this;
            case "NEGATING_SIMPLE_PROPERTY" :
                criteria.withSingleValueCriteria(propertiName, ComparisonOperator.NE, iterator.next(),
                        propertyType);
                return this;
            default :
                throw new IllegalArgumentException("Unsupported keyword " + propertyType);
        }
    }

    @Override
    public DynamoDBQueryCreator<T> and(Class<?> propertyType, String operation, String propertiName, Iterator<Object> iterator) {
        return addCriteria(propertyType, operation, propertiName, iterator);
    }

    @Override
    public List<T> complete(Sort sort) {
        criteria.withSort(sort);
        criteria.withProjection(projection);
        criteria.withLimit(limit);
        return criteria.buildQuery(dynamoDBOperations);
    }
}
