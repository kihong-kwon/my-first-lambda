package net.kkhstudy.myfirstlambda.function;

import net.kkhstudy.myfirstlambda.data.DemoRequest;
import net.kkhstudy.myfirstlambda.entity.IDemoEntity;
import net.kkhstudy.myfirstlambda.repositories.IDemoEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class GetEntityFunction implements Function<Message<String>, Message<IDemoEntity>> {

    @Autowired
    private IDemoEntityDao dao;

    @Override
    public Message<IDemoEntity> apply(Message<String> m) {

        String name = m.getPayload();

        Optional<IDemoEntity> response = dao.getEntity(name);

        Message<IDemoEntity> message = MessageBuilder.withPayload(response.get())
                .setHeader("contentType", "application/json").build();
        return message;
    }
}