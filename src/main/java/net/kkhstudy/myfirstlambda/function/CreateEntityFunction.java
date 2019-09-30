package net.kkhstudy.myfirstlambda.function;

import net.kkhstudy.myfirstlambda.data.DemoRequest;
import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;
import net.kkhstudy.myfirstlambda.repositories.IDemoEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CreateEntityFunction implements Function<Message<DemoRequest>, Message<String>> {

    @Autowired
    private IDemoEntityDao dao;

    @Override
    public Message<String> apply(Message<DemoRequest> m) {
        System.out.println("Start CreateEntityFunction!!!!");
        DemoRequest demo = m.getPayload();
        DynamoDemoEntity entity = new DynamoDemoEntity();
        entity.setName(demo.getName());
        entity.setDescription(demo.getDescription());
        dao.createEntity(entity);
        System.out.println("Created entity with name "
                + entity.getName());
        Message<String> message = MessageBuilder.withPayload("insert success!")
                .setHeader("contentType", "application/json").build();
        return message;
    }
}
