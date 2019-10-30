package net.kkhstudy.myfirstlambda.function;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class GetEntityFunction implements Function<Message<IDemoEntity>, Message<IDemoEntity>> {

    @Override
    public Message<IDemoEntity> apply(Message<IDemoEntity> m) {
        System.out.println("Start GetEntityFunction!!!!");
        IDemoEntity iputEntity = m.getPayload();
        Optional<IDemoEntity> response = Optional.ofNullable(iputEntity);
        System.out.println("Name: " + response.get().getTitle());
        Message<IDemoEntity> message = MessageBuilder.withPayload(response.get())
                .setHeader("contentType", "application/json").build();
        return message;
    }
}