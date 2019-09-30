package net.kkhstudy.myfirstlambda.repositories;

import net.kkhstudy.myfirstlambda.entity.DynamoDemoEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface DynamoDemoEntityRepository extends CrudRepository<DynamoDemoEntity, String> {
}
