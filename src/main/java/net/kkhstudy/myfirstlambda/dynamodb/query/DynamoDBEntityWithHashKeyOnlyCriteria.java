package net.kkhstudy.myfirstlambda.dynamodb.query;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.Select;
import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadataImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DynamoDBEntityWithHashKeyOnlyCriteria<T> extends AbstractDynamoDBQueryCriteria<T> {

    private DynamoDBEntityMetadataImpl<T> entityInformation;

    public DynamoDBEntityWithHashKeyOnlyCriteria(DynamoDBEntityMetadataImpl<T> entityInformation,
                                                 DynamoDBMapperTableModel<T> tableModel) {
        super(entityInformation, tableModel);
        this.entityInformation = entityInformation;
    }

    protected List<T> buildSingleEntityLoadQuery(DynamoDBOperations dynamoDBOperations) {
        T ret = dynamoDBOperations.load(clazz, getHashKeyPropertyValue());
        return new ArrayList<T>(Arrays.asList(ret));
    }

    protected Long buildSingleEntityCountQuery(DynamoDBOperations dynamoDBOperations) {
        return dynamoDBOperations.load(clazz, getHashKeyPropertyValue()) == null ? 0l : 1l;
    }

    protected List<T> buildFinderQuery(DynamoDBOperations dynamoDBOperations) {
        if (isApplicableForGlobalSecondaryIndex()) {

            List<Condition> hashKeyConditions = getHashKeyConditions();
            QueryRequest queryRequest = buildQueryRequest(
                    dynamoDBOperations.getOverriddenTableName(clazz, entityInformation.getDynamoDBTableName()),
                    getGlobalSecondaryIndexName(), getHashKeyAttributeName(), null, null, hashKeyConditions, null);
            return dynamoDBOperations.query(entityInformation.getJavaType(), queryRequest);
        } else {
            return dynamoDBOperations.scan(clazz, buildScanExpression());
        }
    }

    protected Long buildFinderCountQuery(DynamoDBOperations dynamoDBOperations, boolean pageQuery) {
        if (isApplicableForGlobalSecondaryIndex()) {

            List<Condition> hashKeyConditions = getHashKeyConditions();
            QueryRequest queryRequest = buildQueryRequest(
                    dynamoDBOperations.getOverriddenTableName(clazz, entityInformation.getDynamoDBTableName()),
                    getGlobalSecondaryIndexName(), getHashKeyAttributeName(), null, null, hashKeyConditions, null);
            queryRequest.setSelect(Select.COUNT);
            return Long.valueOf(dynamoDBOperations.count(Long.class, queryRequest));
        } else {
            return Long.valueOf(dynamoDBOperations.count(clazz, buildScanExpression()));
        }
    }

    @Override
    protected boolean isOnlyHashKeySpecified() {
        return attributeConditions.size() == 0 && isHashKeySpecified();
    }

    @Override
    public boolean isApplicableForLoad() {
        return isOnlyHashKeySpecified();
    }

    public DynamoDBScanExpression buildScanExpression() {

        // ensureNoSort(sort);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        if (isHashKeySpecified()) {
            scanExpression.addFilterCondition(getHashKeyAttributeName(),
                    createSingleValueCondition(getHashKeyPropertyName(), ComparisonOperator.EQ,
                            getHashKeyAttributeValue(), getHashKeyAttributeValue().getClass(), true));
        }

        for (Map.Entry<String, List<Condition>> conditionEntry : attributeConditions.entrySet()) {
            for (Condition condition : conditionEntry.getValue()) {
                scanExpression.addFilterCondition(conditionEntry.getKey(), condition);
            }
        }

        if (projection.isPresent()) {
            scanExpression.setSelect(Select.SPECIFIC_ATTRIBUTES);
            scanExpression.setProjectionExpression(projection.get());
        }
        limit.ifPresent(scanExpression::setLimit);
        return scanExpression;
    }

    @Override
    public DynamoDBQueryCriteria<T> withPropertyEquals(String propertyName, Object value, Class<?> propertyType) {
        if (isHashKeyProperty(propertyName)) {
            return withHashKeyEquals(value);
        } else {
            Condition condition = createSingleValueCondition(propertyName, ComparisonOperator.EQ, value, propertyType,
                    false);
            return withCondition(propertyName, condition);
        }
    }
}
