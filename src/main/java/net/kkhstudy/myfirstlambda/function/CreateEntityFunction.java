package net.kkhstudy.myfirstlambda.function;

import net.kkhstudy.myfirstlambda.data.DemoRequest;
import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;
import net.kkhstudy.myfirstlambda.repositories.IDemoEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class CreateEntityFunction implements Consumer<Message<DemoRequest>> {

    @Autowired
    private IDemoEntityDao dao;

    @Override
    public void accept(Message<DemoRequest> m) {
        DemoRequest demo = m.getPayload();
        DynamoDemoEntity entity = new DynamoDemoEntity();
        entity.setName(demo.getName());
        entity.setDescription(demo.getDescription());
        dao.createEntity(entity);
        System.out.println("Created entity with name "
                + entity.getName());
    }
}
