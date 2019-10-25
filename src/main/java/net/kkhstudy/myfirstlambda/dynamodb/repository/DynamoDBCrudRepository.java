package net.kkhstudy.myfirstlambda.dynamodb.repository;

import java.util.List;
import java.util.Optional;

public interface DynamoDBCrudRepository<T> {
    Optional<T> findById(T id);
    List<T> findAllById(Iterable<T> ids);
    <S extends T> S save(S entity);
    <S extends T> Iterable<S> saveAll(Iterable<S> entities);
    boolean existsById(T id);
    List<T> findAll();
    long count();
    void deleteById(T id);
    void delete(T entity);
    void deleteAll(Iterable<? extends T> entities);
    void deleteAll();
}
