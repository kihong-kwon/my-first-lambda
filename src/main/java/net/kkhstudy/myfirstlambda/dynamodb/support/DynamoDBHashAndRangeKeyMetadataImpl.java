package net.kkhstudy.myfirstlambda.dynamodb.support;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class DynamoDBHashAndRangeKeyMetadataImpl<T> extends DynamoDBEntityMetadataImpl<T>
        implements DynamoDBHashAndRangeKeyMetadata<T> {

    public DynamoDBHashAndRangeKeyMetadataImpl(final Class<T> domainType) {
        super(domainType);
        ReflectionUtils.doWithMethods(domainType, method -> {
            if (method.getAnnotation(DynamoDBRangeKey.class) != null) {
                String setterMethodName = toSetterMethodNameFromAccessorMethod(method);
                if (setterMethodName != null) {
                    rangeKeySetterMethod = ReflectionUtils.findMethod(domainType, setterMethodName,
                            method.getReturnType());
                    String getterMethodName = toGetterMethodNameFromAccessorMethod(method);
                    rangeKeyGetterMethod = ReflectionUtils.findMethod(domainType, getterMethodName);
                }
            }
        });
        ReflectionUtils.doWithFields(domainType, field -> {
            if (field.getAnnotation(DynamoDBRangeKey.class) != null) {
                rangeKeyField = ReflectionUtils.findField(domainType, field.getName());
            }
        });
    }

    @Override
    public String getRangeKeyPropertyName() {
        return getPropertyNameForAccessorMethod(rangeKeyGetterMethod);
    }

    @Override
    public Set<String> getIndexRangeKeyPropertyNames() {
        final Set<String> propertyNames = new HashSet<>();
        ReflectionUtils.doWithMethods(getJavaType(), method -> {
            if (method.getAnnotation(DynamoDBIndexRangeKey.class) != null) {
                if ((method.getAnnotation(DynamoDBIndexRangeKey.class).localSecondaryIndexName() != null && method
                        .getAnnotation(DynamoDBIndexRangeKey.class).localSecondaryIndexName().trim().length() > 0)
                        || (method.getAnnotation(DynamoDBIndexRangeKey.class).localSecondaryIndexNames() != null
                        && method.getAnnotation(DynamoDBIndexRangeKey.class)
                        .localSecondaryIndexNames().length > 0)) {
                    propertyNames.add(getPropertyNameForAccessorMethod(method));
                }
            }
        });
        ReflectionUtils.doWithFields(getJavaType(), field -> {
            if (field.getAnnotation(DynamoDBIndexRangeKey.class) != null) {
                if ((field.getAnnotation(DynamoDBIndexRangeKey.class).localSecondaryIndexName() != null && field
                        .getAnnotation(DynamoDBIndexRangeKey.class).localSecondaryIndexName().trim().length() > 0)
                        || (field.getAnnotation(DynamoDBIndexRangeKey.class).localSecondaryIndexNames() != null && field
                        .getAnnotation(DynamoDBIndexRangeKey.class).localSecondaryIndexNames().length > 0)) {
                    propertyNames.add(getPropertyNameForField(field));
                }
            }
        });
        return propertyNames;
    }

    public T getHashKeyPropotypeEntityForHashKey(Object hashKey) {
        try {
            T entity = getJavaType().getDeclaredConstructor().newInstance();
            if (hashKeySetterMethod != null) {
                ReflectionUtils.invokeMethod(hashKeySetterMethod, entity, hashKey);
            } else {
                ReflectionUtils.setField(hashKeyField, entity, hashKey);
            }

            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getRangeKey(T id) {
        if (rangeKeyGetterMethod != null) {
            return ReflectionUtils.invokeMethod(rangeKeyGetterMethod, id);
        } else {
            return ReflectionUtils.getField(rangeKeyField, id);
        }
    }
}
