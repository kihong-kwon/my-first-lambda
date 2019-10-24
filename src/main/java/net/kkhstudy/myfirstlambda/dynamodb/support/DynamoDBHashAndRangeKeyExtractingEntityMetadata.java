package net.kkhstudy.myfirstlambda.dynamodb.support;

import java.util.Set;

public interface DynamoDBHashAndRangeKeyExtractingEntityMetadata<T, ID> extends DynamoDBHashKeyExtractingEntityMetadata {

    <H> HashAndRangeKeyExtractor<ID, H> getHashAndRangeKeyExtractor(Class<ID> idClass);

    String getRangeKeyPropertyName();

    Set<String> getIndexRangeKeyPropertyNames();

    boolean isCompositeHashAndRangeKeyProperty(String propertyName);

    <H> T getHashKeyPropotypeEntityForHashKey(H hashKey);
}
