package net.kkhstudy.myfirstlambda.dynamodb.query;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadataImpl;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBHashAndRangeKeyMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class AbstractDynamoDBQueryCreator<T> {

    protected final DynamoDBEntityMetadataImpl<T> entityMetadata;
    protected final DynamoDBOperations dynamoDBOperations;
    protected final Optional<String> projection;
    protected final Optional<Integer> limit;

    public AbstractDynamoDBQueryCreator(DynamoDBEntityMetadataImpl<T> entityMetadata,
                                        Optional<String> projection, Optional<Integer> limitResults, DynamoDBOperations dynamoDBOperations) {
        //super(tree);
        this.entityMetadata = entityMetadata;
        this.projection = projection;
        this.limit = limitResults;
        this.dynamoDBOperations = dynamoDBOperations;
    }

    public DynamoDBQueryCriteria<T> addCriteria(Class<?> propertyType, String operation, String propertiName,
                                                       Iterator<Object> iterator) {
        final DynamoDBMapperTableModel<T> tableModel = dynamoDBOperations.getTableModel(entityMetadata.getJavaType());
        DynamoDBQueryCriteria<T> criteria = entityMetadata.isRangeKeyAware()
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
                return criteria.withPropertyIn(propertiName, iterable, propertyType);
            case "CONTAINING" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.CONTAINS,
                        iterator.next(), propertyType);
            case "STARTING_WITH" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.BEGINS_WITH,
                        iterator.next(), propertyType);
            case "BETWEEN" :
                Object first = iterator.next();
                Object second = iterator.next();
                return criteria.withPropertyBetween(propertiName, first, second, propertyType);
            case "AFTER" :
            case "GREATER_THAN" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.GT, iterator.next(),
                        propertyType);
            case "BEFORE" :
            case "LESS_THAN" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.LT, iterator.next(),
                        propertyType);
            case "GREATER_THAN_EQUAL" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.GE, iterator.next(),
                        propertyType);
            case "LESS_THAN_EQUAL" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.LE, iterator.next(),
                        propertyType);
            case "IS_NULL" :
                return criteria.withNoValuedCriteria(propertiName, ComparisonOperator.NULL);
            case "IS_NOT_NULL" :
                return criteria.withNoValuedCriteria(propertiName, ComparisonOperator.NOT_NULL);
            case "TRUE" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.EQ, Boolean.TRUE,
                        propertyType);
            case "FALSE" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.EQ, Boolean.FALSE,
                        propertyType);
            case "SIMPLE_PROPERTY" :
                return criteria.withPropertyEquals(propertiName, iterator.next(), propertyType);
            case "NEGATING_SIMPLE_PROPERTY" :
                return criteria.withSingleValueCriteria(propertiName, ComparisonOperator.NE, iterator.next(),
                        propertyType);
            default :
                throw new IllegalArgumentException("Unsupported keyword " + propertyType);
        }
    }
}
