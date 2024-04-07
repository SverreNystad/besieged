package com.softwarearchitecture.networking.persistence;

import java.util.List;
import java.util.Optional;

/**
 * Represents a Data Access Object (DAO), following the Data Access Object pattern.
 * This abstract base class abstracts the interaction with the data storage system, allowing for operations
 * on stored data without exposing the details of the implementation. It provides a standardized
 * method to access, modify, and manage data across different types of storage mechanisms.
 * 
 * @param <K> The type of the primary key used to identify objects of type T.
 * @param <T> The type of the object managed by this DAO.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Data_access_object">Data Access Object on Wikipedia</a>
 */
public abstract class DAO<K, T> {
    protected boolean create;
    protected boolean read;
    protected boolean update;
    protected boolean delete;

    /**
     * Indicates whether this DAO implementation supports the creation (addition) of new instances.
     * 
     * @return {@code true} if the DAO supports creating new instances; {@code false} otherwise.
     */
    public boolean canCreate() {
        return create;
    }
    
    /**
     * Indicates whether this DAO implementation supports reading (retrieving) instances.
     * 
     * @return {@code true} if the DAO supports reading instances; {@code false} otherwise.
     */
    public boolean canRead() {
        return read;
    }

    /**
     * Indicates whether this DAO implementation supports updating existing instances.
     * 
     * @return {@code true} if the DAO supports updating instances; {@code false} otherwise.
     */
    public boolean canUpdate() {
        return update;
    }

    /**
     * Indicates whether this DAO implementation supports deleting instances.
     * 
     * @return {@code true} if the DAO supports deleting instances; {@code false} otherwise.
     */
    public boolean canDelete() {
        return delete;
    }

    /**
     * Retrieves all instances of type {@code T} from the data storage.
     * 
     * @return A {@link List} containing all instances of type {@code T} found in the data storage;
     *         an empty list if no instances are found.
     */
    public abstract List<T> loadAll();

    /**
     * Finds and returns an {@link Optional} wrapper for the instance of type {@code T} identified by the given primary key {@code id}.
     * 
     * This method provides a way to safely access instances without risking a {@code NullPointerException}. If no instance is found
     * for the specified {@code id}, an empty {@link Optional} is returned.
     * 
     * @param id The primary key used to identify the instance in the data storage.
     * @return An {@link Optional} containing the instance of type {@code T} associated with the specified {@code id}.
     *         If no such instance exists, an empty {@link Optional} is returned.
     */
    public abstract Optional<T> get(K id);

    /**
     * Updates the instance of type {@code T} identified by the given primary key {@code id} with
     * the provided {@code object} data. 
     * 
     * @param id The primary key used to identify the instance to be updated.
     * @param object The updated instance of type {@code T} to replace the existing one.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     */
    public abstract boolean update(K id, T object);

    /**
     * Deletes the instance of type {@code T} identified by the given primary key {@code id}.
     * 
     * @param id The primary key used to identify the instance to be deleted.
     * @return {@code true} if the deletion was successful; {@code false} otherwise.
     */
    public abstract boolean delete(K id);

    /**
     * Adds a new instance of type {@code T} to the data storage. If an instance with the same primary key
     * already exists, the operation will not proceed.
     * 
     * @param id The primary key used to identify the instance to be added.
     * @param object The instance of type {@code T} to be added to the data storage.
     */
    public abstract void add(K id, T object);

}
