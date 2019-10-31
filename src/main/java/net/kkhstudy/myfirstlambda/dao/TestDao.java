package net.kkhstudy.myfirstlambda.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import net.kkhstudy.myfirstlambda.entity.TitleAuthorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestDao {

    // query    ：PaginatedQueryListを返します
    // queryPage：QueryResultPageを返します
    // scan     ：PaginatedScanListを返します
    // scanPage ：ScanResultPageを返します

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void createEntity(TitleAuthorEntity entity) {
        dynamoDBMapper.save(entity);
    }

    public void deleteEntity(TitleAuthorEntity entity) {
        dynamoDBMapper.delete(entity);
    }

    public TitleAuthorEntity getDemoEntity(String title) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .build();
        TitleAuthorEntity entity = dynamoDBMapper.load(TitleAuthorEntity.class, title, config);
        return entity;
    }

    public TitleAuthorEntity getDemoEntity(String title, String author) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .build();
        TitleAuthorEntity entity = dynamoDBMapper.load(TitleAuthorEntity.class, title, author, config);
        return entity;
    }

    public QueryResultPage<TitleAuthorEntity> getDemoEntityListUsingQueryPage(String title) {
        // queryPageはKey属性に対して検索条件に設定可能。
        TitleAuthorEntity entity = new TitleAuthorEntity();
        entity.setTitle(title);
        DynamoDBQueryExpression<TitleAuthorEntity> queryExpression = new DynamoDBQueryExpression<TitleAuthorEntity>()
                .withScanIndexForward(true) // true: ascending, false: descending
                // .withIndexName() // indexから検索する場合
                .withHashKeyValues(entity)
                .withLimit(5);
        QueryResultPage<TitleAuthorEntity> itemList = dynamoDBMapper.queryPage(TitleAuthorEntity.class, queryExpression);
        return itemList;
    }

    public void getDemoEntityListUsingScanPage(String title) {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":n", new AttributeValue().withN("100"));

        // scanPageはKey属性以外も検索条件に設定可能。
        TitleAuthorEntity entity = new TitleAuthorEntity();
        entity.setTitle(title);
        DynamoDBScanExpression scanPageExpression = new DynamoDBScanExpression()
                .withLimit(5)
                // .withIndexName() // indexから検索する場合
                .withFilterExpression("price >= :n")
                .withExpressionAttributeValues(eav);

        do {
            ScanResultPage<TitleAuthorEntity> scanPage = dynamoDBMapper.scanPage(TitleAuthorEntity.class, scanPageExpression);
            System.out.println("LastEvaluatedKey=" + scanPage.getLastEvaluatedKey());
            scanPageExpression.setExclusiveStartKey(scanPage.getLastEvaluatedKey());
        } while (scanPageExpression.getExclusiveStartKey() != null);

        //return itemList;
    }

    public int getCountDemoEntityList(String title) {
        TitleAuthorEntity entity = new TitleAuthorEntity();
        entity.setTitle(title);
        DynamoDBQueryExpression<TitleAuthorEntity> queryExpression = new DynamoDBQueryExpression<TitleAuthorEntity>()
                .withHashKeyValues(entity);
        int count = dynamoDBMapper.count(TitleAuthorEntity.class, queryExpression);
        return count;
    }
}
