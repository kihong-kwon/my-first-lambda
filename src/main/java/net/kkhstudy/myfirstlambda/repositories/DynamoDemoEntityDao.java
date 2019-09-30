package net.kkhstudy.myfirstlambda.repositories;

import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;
import net.kkhstudy.myfirstlambda.entity.IDemoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DynamoDemoEntityDao implements IDemoEntityDao {

    @Autowired
    private DynamoDemoEntityRepository repository;

    @Override
    public void createEntity(IDemoEntity entity) {
        repository.save((DynamoDemoEntity) entity);
    }

    @Override
    public Optional<IDemoEntity> getEntity(String name) {
        Optional<DynamoDemoEntity> entity = repository.findById(name);
        return Optional.of((IDemoEntity) entity.get());
    }

    @Override
    public void updateEntity(IDemoEntity entity) {
        repository.save((DynamoDemoEntity) entity);
    }

    @Override
    public void deleteEntity(String name) {
        repository.deleteById(name);
    }
}
