package net.kkhstudy.myfirstlambda.function;

import lombok.extern.slf4j.Slf4j;
import net.kkhstudy.myfirstlambda.entity.IDemoEntity;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class ConsumerFunction implements Consumer<Message<IDemoEntity>> {

    @Override
    public void accept(Message<IDemoEntity> m) {
        System.out.println("Created entity with name " + m.getPayload().getName());
    }
}
