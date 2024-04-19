package com.softwarearchitecture.networking.persistence;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalDAO<K,T> implements DAO<K,T> {

    private static ConcurrentHashMap<String, LocalDAO<?, ?>> instances = new ConcurrentHashMap<>();

    private final String key;

    private ConcurrentHashMap<K, T> data;

    private LocalDAO(String key) {
        this.data = new ConcurrentHashMap<>();
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public static synchronized <K, T> LocalDAO<K, T> getInstance(Class<K> kClass, Class<T> tClass) {
        String key = kClass.getName() + tClass.getName();
        System.out.println("Number of instances: " + instances.size() + ". " + "Key: " + key);
        return (LocalDAO<K, T>) instances.computeIfAbsent(key, k -> new LocalDAO<>(key));
    }

    @Override
    public List<K> loadAllIndices() {
        return List.copyOf(data.keySet());
    }

    @Override
    public Optional<T> get(K id) {
        // System.out.println("Getting data for key: " + key + ". Size: " + data.size());
        if (this.key == "java.lang.String[B") {
            System.out.println("Getting data for key: " + id + ". Size: " + data.size());
        }
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean update(K id, T object) {
        if (this.key == "java.lang.String[B") {
            System.out.println("Updating data for key: " + id + ". Size: " + data.size());
        }
        return data.replace(id, object) != null;
    }

    @Override
    public boolean delete(K id) {
        return data.remove(id) != null;
    }

    @Override
    public void add(K id, T object) {
        if (this.key.equals("java.lang.String[B")) {
            System.out.println("Adding data for key: " + id + ". Size: " + data.size());
        }
        data.put(id, object);
    }
}
