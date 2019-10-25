package net.kkhstudy.myfirstlambda.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import net.kkhstudy.myfirstlambda.dynamodb.core.DynamoDBOperations;
import net.kkhstudy.myfirstlambda.dynamodb.support.DynamoDBEntityMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SimpleDynamoDBCrudRepository<T> implements DynamoDBCrudRepository<T> {
    protected DynamoDBEntityMetadata<T> dynamoDBEntityMetadata;

    protected Class<T> domainType;

    protected DynamoDBOperations dynamoDBOperations;

    public SimpleDynamoDBCrudRepository(DynamoDBEntityMetadata<T> dynamoDBEntityMetadata,
                                        DynamoDBOperations dynamoDBOperations) {
        this.dynamoDBEntityMetadata = dynamoDBEntityMetadata;
        this.dynamoDBOperations = dynamoDBOperations;
        this.domainType = dynamoDBEntityMetadata.getJavaType();
    }

    @Override
    public Optional<T> findById(T id) {

        Assert.notNull(id, "The given id must not be null!");

        T result;
        if (dynamoDBEntityMetadata.isRangeKeyAware()) {
            result = dynamoDBOperations.load(domainType, dynamoDBEntityMetadata.getHashKey(id),
                    dynamoDBEntityMetadata.getRangeKey(id));
        } else {
            result = dynamoDBOperations.load(domainType, dynamoDBEntityMetadata.getHashKey(id));
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<T> findAllById(Iterable<T> ids) {

        Assert.notNull(ids, "The given ids must not be null!");

        // Works only with non-parallel streams!
        AtomicInteger idx = new AtomicInteger();
        List<KeyPair> keyPairs = StreamSupport.stream(ids.spliterator(), false).map(id -> {

            Assert.notNull(id, "The given id at position " + idx.getAndIncrement() + " must not be null!");

            if (dynamoDBEntityMetadata.isRangeKeyAware()) {
                return new KeyPair().withHashKey(dynamoDBEntityMetadata.getHashKey(id))
                        .withRangeKey(dynamoDBEntityMetadata.getRangeKey(id));
            } else {
                return new KeyPair().withHashKey(id);
            }
        }).collect(Collectors.toList());

        Map<Class<?>, List<KeyPair>> keyPairsMap = Collections.<Class<?>, List<KeyPair>>singletonMap(domainType,
                keyPairs);
        return dynamoDBOperations.batchLoad(keyPairsMap);
    }

    @Override
    public <S extends T> S save(S entity) {

        dynamoDBOperations.save(entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
            //throws BatchWriteException, IllegalArgumentException {

        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBOperations.batchSave(entities);

        if (failedBatches.isEmpty()) {
            // Happy path
            return entities;
        } else {
            // TODO Exception
            //throw repackageToException(failedBatches, BatchWriteException.class);
            return null;
        }
    }

    @Override
    public boolean existsById(T id) {

        Assert.notNull(id, "The given id must not be null!");
        return findById(id).isPresent();
    }

    void assertScanEnabled(boolean scanEnabled, String methodName) {
        Assert.isTrue(scanEnabled, "Scanning for unpaginated " + methodName + "() queries is not enabled.  "
                + "To enable, re-implement the " + methodName
                + "() method in your repository interface and annotate with @EnableScan, or "
                + "enable scanning for all repository methods by annotating your repository interface with @EnableScan");
    }

    @Override
    public List<T> findAll() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBOperations.scan(domainType, scanExpression);
    }

    @Override
    public long count() {
        final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBOperations.count(domainType, scanExpression);
    }

    @Override
    public void deleteById(T id) {

        Assert.notNull(id, "The given id must not be null!");

        Optional<T> entity = findById(id);

        if (entity.isPresent()) {
            dynamoDBOperations.delete(entity.get());

        } else {
            // TODO Exception
            // throw new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!", domainType, id), 1);
        }
    }

    @Override
    public void delete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        dynamoDBOperations.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");
        dynamoDBOperations.batchDelete(entities);
    }

    @Override
    public void deleteAll() {
        dynamoDBOperations.batchDelete(findAll());
    }

    @NonNull
    public DynamoDBEntityMetadata<T> getDynamoDBEntityMetadata() {
        return this.dynamoDBEntityMetadata;
    }
}
