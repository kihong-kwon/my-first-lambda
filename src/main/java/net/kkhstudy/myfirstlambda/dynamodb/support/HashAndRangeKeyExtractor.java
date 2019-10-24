package net.kkhstudy.myfirstlambda.dynamodb.support;

public interface HashAndRangeKeyExtractor<ID, H> extends HashKeyExtractor<ID, H> {
    Object getRangeKey(ID id);
}
