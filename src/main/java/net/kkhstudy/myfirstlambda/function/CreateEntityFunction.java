package net.kkhstudy.myfirstlambda.function;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTransactionWriteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;
import net.kkhstudy.myfirstlambda.data.DemoRequest;
import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class CreateEntityFunction implements Function<Message<DemoRequest>, Message<String>> {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public Message<String> apply(Message<DemoRequest> m) {

        System.out.println("Start CreateEntityFunction!!!!");
        DemoRequest demo = m.getPayload();
        DynamoDemoEntity entity1 = new DynamoDemoEntity();
        entity1.setTitle("test1");
        entity1.setDescription("test1");

        DynamoDemoEntity entity2 = new DynamoDemoEntity();
        entity2.setTitle(demo.getTitle());
        entity2.setDescription(demo.getDescription());

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS("test2"));

        DynamoDBQueryExpression<DynamoDemoEntity> queryExpression = new DynamoDBQueryExpression<DynamoDemoEntity>()
                .withKeyConditionExpression("title = :v1")
                .withExpressionAttributeValues(eav);

        List<DynamoDemoEntity> getEntity = dynamoDBMapper.query(DynamoDemoEntity.class, queryExpression);

        if (!getEntity.isEmpty()) {
            System.out.println("query:" + getEntity.get(0).getTitle());
        } else {
            System.out.println("Get nothing!");
        }

        DynamoDBTransactionWriteExpression conditionExpressionForConditionCheck = new DynamoDBTransactionWriteExpression()
                //.withConditionExpression("attribute_exists(description)")
                .withConditionExpression("title = :v1")
                .withExpressionAttributeValues(eav);

        DynamoDemoEntity entity3 = new DynamoDemoEntity();
        entity3.setTitle("test3");
        entity3.setDescription("test3");

        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
        transactionWriteRequest.addPut(entity1);
        transactionWriteRequest.addConditionCheck(entity2, conditionExpressionForConditionCheck); // DB上に条件に合致するデータが存在する場合、成功する。
        transactionWriteRequest.addPut(entity3);
        executeTransactionWrite(transactionWriteRequest);

        System.out.println("Created entity with name "
                + entity1.getTitle());
        Message<String> message = MessageBuilder.withPayload("insert success!")
                .setHeader("contentType", "application/json").build();
        System.out.println("Insert success!");

        return message;
    }

    private void executeTransactionWrite(TransactionWriteRequest transactionWriteRequest) {
        try {
            dynamoDBMapper.transactionWrite(transactionWriteRequest);
        } catch (DynamoDBMappingException ddbme) {
            System.err.println("Client side error in Mapper, fix before retrying. Error: " + ddbme.getMessage());
        } catch (ResourceNotFoundException rnfe) {
            System.err.println("One of the tables was not found, verify table exists before retrying. Error: " + rnfe.getMessage());
        } catch (InternalServerErrorException ise) {
            System.err.println("Internal Server Error, generally safe to retry with back-off. Error: " + ise.getMessage());
        } catch (TransactionCanceledException tce) {
            System.err.println("Transaction Canceled, implies a client issue, fix before retrying. Error: " + tce.getMessage());
        } catch (Exception ex) {
            System.err.println("An exception occurred, investigate and configure retry strategy. Error: " + ex.getMessage());
        }
    }
}
