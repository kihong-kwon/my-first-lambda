package net.kkhstudy.myfirstlambda.function;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTransactionWriteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;
import net.kkhstudy.myfirstlambda.data.DemoRequest;
import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CreateEntityFunction implements Function<Message<DemoRequest>, Message<String>> {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public Message<String> apply(Message<DemoRequest> m) {

        //dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        System.out.println("Start CreateEntityFunction!!!!");
        DemoRequest demo = m.getPayload();
        DynamoDemoEntity entity1 = new DynamoDemoEntity();
        entity1.setName("test1");
        entity1.setDescription("test2");
        //dynamoDBMapper.save(entity);
        DynamoDemoEntity entity2 = new DynamoDemoEntity();
        entity2.setName("test");
        entity2.setDescription("test");
        DynamoDBTransactionWriteExpression conditionExpressionForConditionCheck = new DynamoDBTransactionWriteExpression()
                .withConditionExpression("attribute_exists(Name)");

        DynamoDemoEntity entity3 = new DynamoDemoEntity();
        entity3.setName("test3");
        entity3.setDescription("test3");

        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
        transactionWriteRequest.addPut(entity1);
        transactionWriteRequest.addConditionCheck(entity2, conditionExpressionForConditionCheck);
        transactionWriteRequest.addPut(entity3);
        executeTransactionWrite(transactionWriteRequest);

        System.out.println("Created entity with name "
                + entity1.getName());
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
