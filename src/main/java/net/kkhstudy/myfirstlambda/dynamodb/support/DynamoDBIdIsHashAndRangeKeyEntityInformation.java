package net.kkhstudy.myfirstlambda.dynamodb.support;

public interface DynamoDBIdIsHashAndRangeKeyEntityInformation<T, ID> extends
        DynamoDBHashAndRangeKeyExtractingEntityMetadata<T, ID>,
        DynamoDBEntityInformation<ID> {
}
