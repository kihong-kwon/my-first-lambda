package net.kkhstudy.myfirstlambda.dynamodb.support;

import java.util.Set;

public interface DynamoDBHashAndRangeKeyMetadata<T> extends DynamoDBEntityMetadata<T> {

    String getRangeKeyPropertyName();

    Set<String> getIndexRangeKeyPropertyNames();

    <H> T getHashKeyPropotypeEntityForHashKey(H hashKey);

}
