package com.softwarearchitecture.networking.persistence;

import java.util.List;

/**
 * Represents a Data Access Object (DAO) interface, following the Data Access Object pattern.
 * This interface abstracts the interaction with the data storage system, allowing for operations
 * on stored data without exposing the details of the implementation. It provides a standardized
 * method to access, modify, and manage data across different types of storage mechanisms.
 * 
 * @param <T> The type of the object managed by this DAO.
 * @param <K> The type of the primary key used to identify objects of type T.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Data_access_object">Data Access Object on Wikipedia</a>
 */
public interface DAO<T, K> {

    /**
     * Indicates whether this DAO implementation supports the creation (addition) of new instances.
     * 
     * @return {@code true} if the DAO supports creating new instances; {@code false} otherwise.
     */
    public boolean canCreate();

    /**
     * Indicates whether this DAO implementation supports reading (retrieving) instances.
     * 
     * @return {@code true} if the DAO supports reading instances; {@code false} otherwise.
     */
    public boolean canRead();

    /**
     * Indicates whether this DAO implementation supports updating existing instances.
     * 
     * @return {@code true} if the DAO supports updating instances; {@code false} otherwise.
     */
    public boolean canUpdate();

    /**
     * Indicates whether this DAO implementation supports deleting instances.
     * 
     * @return {@code true} if the DAO supports deleting instances; {@code false} otherwise.
     */
    public boolean canDelete();

    /**
     * Retrieves all instances of type {@code T} from the data storage.
     * 
     * @return A {@link List} containing all instances of type {@code T} found in the data storage;
     *         an empty list if no instances are found.
     */
    public List<T> loadAll();

    /**
     * Finds and returns the instance of type {@code T} identified by the given primary key {@code id}.
     * 
     * @param id The primary key used to identify the instance in the data storage.
     * @return The instance of type {@code T} associated with the specified {@code id};
     *         {@code null} if no such instance exists.
     */
    public T get(K id);

    /**
     * Updates the instance of type {@code T} identified by the given primary key {@code id} with
     * the provided {@code object} data. 
     * 
     * @param id The primary key used to identify the instance to be updated.
     * @param object The updated instance of type {@code T} to replace the existing one.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     */
    public boolean update(K id, T object);

    /**
     * Deletes the instance of type {@code T} identified by the given primary key {@code id}.
     * 
     * @param id The primary key used to identify the instance to be deleted.
     * @return {@code true} if the deletion was successful; {@code false} otherwise.
     */
    public boolean delete(K id);

    /**
     * Adds a new instance of type {@code T} to the data storage. If an instance with the same primary key
     * already exists, the operation will not proceed.
     * 
     * @param object The instance of type {@code T} to be added to the data storage.
     * @return {@code true} if the addition was successful; {@code false} otherwise.
     */
    public boolean add(T object);

}
