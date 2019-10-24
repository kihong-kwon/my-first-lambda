package net.kkhstudy.myfirstlambda.dynamodb.support;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DynamoDBIdIsHashAndRangeKeyEntityInformationImpl<T, ID> implements
        DynamoDBIdIsHashAndRangeKeyEntityInformation<T, ID> {


    private DynamoDBHashAndRangeKeyExtractingEntityMetadata<T, ID> metadata;
    private HashAndRangeKeyExtractor<ID, ?> hashAndRangeKeyExtractor;
    private Optional<String> projection = Optional.empty();
    private Optional<Integer> limit = Optional.empty();

    public DynamoDBIdIsHashAndRangeKeyEntityInformationImpl(Class<T> domainClass,
                                                            DynamoDBHashAndRangeKeyExtractingEntityMetadata<T, ID> metadata) {
        super(domainClass, Id.class);
        this.metadata = metadata;
        this.hashAndRangeKeyExtractor = metadata.getHashAndRangeKeyExtractor(getIdType());
    }

    @Override
    public Optional<String> getProjection() {
        return projection;
    }

    @Override
    public Optional<Integer> getLimit() {
        return limit;
    }

    @Override
    public boolean isRangeKeyAware() {
        return true;
    }

    @Override
    public Object getHashKey(final ID id) {
        return hashAndRangeKeyExtractor.getHashKey(id);
    }

    @Override
    public Object getRangeKey(final ID id) {
        return hashAndRangeKeyExtractor.getRangeKey(id);
    }

    @Override
    public Optional<String> getOverriddenAttributeName(String attributeName) {
        return metadata.getOverriddenAttributeName(attributeName);
    }

    @Override
    public boolean isHashKeyProperty(String propertyName) {
        return metadata.isHashKeyProperty(propertyName);
    }

    @Override
    public boolean isCompositeHashAndRangeKeyProperty(String propertyName) {
        return metadata.isCompositeHashAndRangeKeyProperty(propertyName);
    }

    @Override
    public String getRangeKeyPropertyName() {
        return metadata.getRangeKeyPropertyName();
    }

    @Override
    public DynamoDBTypeConverter<?, ?> getTypeConverterForProperty(String propertyName) {
        return metadata.getTypeConverterForProperty(propertyName);
    }

    @Override
    public Set<String> getIndexRangeKeyPropertyNames() {
        return metadata.getIndexRangeKeyPropertyNames();
    }

    @Override
    public String getHashKeyPropertyName() {
        return metadata.getHashKeyPropertyName();
    }

    @Override
    public <H> HashAndRangeKeyExtractor<ID, H> getHashAndRangeKeyExtractor(Class<ID> idClass) {
        return metadata.getHashAndRangeKeyExtractor(idClass);
    }

    @Override
    public String getDynamoDBTableName() {
        return metadata.getDynamoDBTableName();
    }

    @Override
    public Map<String, String[]> getGlobalSecondaryIndexNamesByPropertyName() {
        return metadata.getGlobalSecondaryIndexNamesByPropertyName();
    }

    @Override
    public <H> T getHashKeyPropotypeEntityForHashKey(H hashKey) {
        return metadata.getHashKeyPropotypeEntityForHashKey(hashKey);
    }

    @Override
    public boolean isGlobalIndexHashKeyProperty(String propertyName) {
        return metadata.isGlobalIndexHashKeyProperty(propertyName);
    }

    @Override
    public boolean isGlobalIndexRangeKeyProperty(String propertyName) {
        return metadata.isGlobalIndexRangeKeyProperty(propertyName);
    }
}
