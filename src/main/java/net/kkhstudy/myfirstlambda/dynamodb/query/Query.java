package net.kkhstudy.myfirstlambda.dynamodb.query;

import java.util.List;

public interface Query<T> {
    List<T> getResultList();

    T getSingleResult();

    void setScanEnabled(boolean scanEnabled);
    void setScanCountEnabled(boolean scanCountEnabled);
    boolean isScanCountEnabled();
    boolean isScanEnabled();
}
