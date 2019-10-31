package net.kkhstudy.myfirstlambda.function;

import net.kkhstudy.myfirstlambda.entity.TitleAuthorEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class GetEntityFunction implements Function<Message<TitleAuthorEntity>, Message<TitleAuthorEntity>> {

    @Override
    public Message<TitleAuthorEntity> apply(Message<TitleAuthorEntity> m) {
        System.out.println("Start GetEntityFunction!!!!");
        TitleAuthorEntity iputEntity = m.getPayload();
        Optional<TitleAuthorEntity> response = Optional.ofNullable(iputEntity);
        System.out.println("Name: " + response.get().getTitle());
        Message<TitleAuthorEntity> message = MessageBuilder.withPayload(response.get())
                .setHeader("contentType", "application/json").build();
        return message;
    }
}