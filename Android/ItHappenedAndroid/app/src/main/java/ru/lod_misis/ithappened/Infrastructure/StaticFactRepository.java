package ru.lod_misis.ithappened.Infrastructure;

/**
 * Created by Пользователь on 30.03.2018.
 */

public class StaticFactRepository {
    private static final InMemoryFactRepository ourInstance = new InMemoryFactRepository();

    public static InMemoryFactRepository getInstance() {
        return ourInstance;
    }

    private StaticFactRepository() {
    }
}
