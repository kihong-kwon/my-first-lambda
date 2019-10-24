package net.kkhstudy.myfirstlambda.dynamodb.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface DynamoDBHashAndRangeKeyMethodExtractor<T> {
    Method getHashKeyMethod();

    Method getRangeKeyMethod();

    Field getHashKeyField();

    Field getRangeKeyField();

    Class<T> getJavaType();
}
