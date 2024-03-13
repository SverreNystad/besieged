package com.softwarearchitecture.networking.persistence;

import java.util.List;

public class DAOFirebase<T,K> implements DAO<T,K> {

    @Override
    public boolean canCreate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canCreate'");
    }

    @Override
    public boolean canRead() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canRead'");
    }

    @Override
    public boolean canUpdate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canUpdate'");
    }

    @Override
    public boolean canDelete() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canDelete'");
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
