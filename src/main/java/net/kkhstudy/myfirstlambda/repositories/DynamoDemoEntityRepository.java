package net.kkhstudy.myfirstlambda.repositories;

import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamoDemoEntityRepository extends CrudRepository<DynamoDemoEntity, String> {
}
