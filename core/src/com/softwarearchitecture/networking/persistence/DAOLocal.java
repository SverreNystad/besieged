package com.softwarearchitecture.networking.persistence;

import java.util.List;

public class DAOLocal<T,K> implements DAO<T,K> {
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;

    public DAOLocal(boolean create, boolean read, boolean update, boolean delete) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadAll'");
    }

    @Override
    public T get(K id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public boolean update(K id, T object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(K id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public boolean add(T object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }
}
