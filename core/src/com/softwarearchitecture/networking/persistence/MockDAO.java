package com.softwarearchitecture.networking.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockDAO<T, K> implements DAO<T, K>{

    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;
    
    // In-memory storage for entities, using a Map to simulate database storage.
    private Map<K, T> storage = new HashMap<>();

    // Simulating key generation.
    private Integer nextId = 0;
    
    public MockDAO(boolean create, boolean read, boolean update, boolean delete) {
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
    }

    @Override
    public boolean canCreate() {
        return create;
    }

    @Override
    public boolean canRead() {
        return read;
    }

    @Override
    public boolean canUpdate() {
        return update;
    }

    @Override
    public boolean canDelete() {
        return delete;
    }
 @Override
    public List<T> loadAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public T get(K id) {
        return storage.get(id);
    }

    @Override
    public boolean update(K id, T object) {
        if (storage.containsKey(id)) {
            storage.put(id, object);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(K id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public K add(T object) {
        // Simulating key generation and ensuring type safety via casting.
        // This casting is only safe if K is compatible with the type of nextId's value.
        K key = (K) nextId;
        this.nextId++;
        storage.put(key, object);
        return key;
    }

}
