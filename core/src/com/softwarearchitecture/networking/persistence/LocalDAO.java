package com.softwarearchitecture.networking.persistence;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalDAO<K,T> implements DAO<K,T> {

    private static ConcurrentHashMap<String, LocalDAO<?, ?>> instances = new ConcurrentHashMap<>();

    private ConcurrentHashMap<K, T> data;

    private LocalDAO(String key) {
        this.data = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static synchronized <K, T> LocalDAO<K, T> getInstance(Class<K> kClass, Class<T> tClass) {
        String key = kClass.getName() + tClass.getName();
        return (LocalDAO<K, T>) instances.computeIfAbsent(key, k -> new LocalDAO<>(key));
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
