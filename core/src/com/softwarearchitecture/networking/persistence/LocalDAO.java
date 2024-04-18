package com.softwarearchitecture.networking.persistence;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalDAO<K,T> implements DAO<K,T> {

    private ConcurrentHashMap<K, T> data;

    public LocalDAO() {
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public List<K> loadAllIndices() {
        return List.copyOf(data.keySet());
    }

    @Override
    public Optional<T> get(K id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean update(K id, T object) {
        return data.replace(id, object) != null;
    }

    @Override
    public boolean delete(K id) {
        return data.remove(id) != null;
    }

    @Override
    public void add(K id, T object) {
        data.put(id, object);
    }
}
