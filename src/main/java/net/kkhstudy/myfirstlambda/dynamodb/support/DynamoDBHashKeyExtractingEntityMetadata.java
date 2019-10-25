package net.kkhstudy.myfirstlambda.dynamodb.support;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.Map;
import java.util.Optional;

public interface DynamoDBHashKeyExtractingEntityMetadata<T> {
    Optional<String> getOverriddenAttributeName(String propertyName);

    DynamoDBTypeConverter<?, ?> getTypeConverterForProperty(String propertyName);

    boolean isHashKeyProperty(String propertyName);

    String getHashKeyPropertyName();

    String getDynamoDBTableName();

    Map<String, String[]> getGlobalSecondaryIndexNamesByPropertyName();

    boolean isGlobalIndexHashKeyProperty(String propertyName);

    boolean isGlobalIndexRangeKeyProperty(String propertyName);

    Class<T> getJavaType();

    Object getHashKey(T id);

    default Object getRangeKey(T id) {
        return null;
    }

    boolean isRangeKeyAware();
}
