package net.kkhstudy.myfirstlambda.dynamodb.support;

public interface HashKeyExtractor<ID, H> {
    H getHashKey(ID id);
}
