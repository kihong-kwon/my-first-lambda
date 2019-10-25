package net.kkhstudy.myfirstlambda.dynamodb.support;

import java.util.Set;

public interface DynamoDBHashAndRangeKeyExtractingEntityMetadata<T> extends DynamoDBHashKeyExtractingEntityMetadata<T> {

    String getRangeKeyPropertyName();

    Set<String> getIndexRangeKeyPropertyNames();

    <H> T getHashKeyPropotypeEntityForHashKey(H hashKey);

}
