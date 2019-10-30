package net.kkhstudy.myfirstlambda.dynamodb.query;

import java.util.Iterator;
import java.util.List;

public interface DynamoDBQueryCreator<T> {

    DynamoDBQueryCreator<T> addCriteria(Class<?> propertyType, String operation, String propertiName, Iterator<Object> iterator);

    List<T> complete(Sort sort);

    DynamoDBQueryCreator<T> and(Class<?> propertyType, String operation, String propertiName, Iterator<Object> iterator);

}
