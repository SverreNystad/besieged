package com.softwarearchitecture.networking.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A builder class for creating instances of Data Access Objects ({@link DAO}s) with 
 * 
 * @param <K> The type of the primary key used by the DAO to identify entities.
 * @param <T> The type of the entity object managed by the DAO.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern on Wikipedia</a>
 */
public class DAOBuilder<K, T>{
    /**
     * Constructs and returns a DAO instance configured according to the builder settings.
     * This method decides the specific DAO implementation to instantiate based on the
     * configuration provided. Currently, defaults to {@code FireBaseDAO}.
     * 
     * If it is not possible to create the DAO instance, the method will terminate the application.
     * 
     * @return A configured DAO instance.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DAO<K, T> build(Class<K> idParameterClass, Class<T> typeParameterClass) {
        // TODO: add conditional logic to determine which DAO implementation to use
        // based on the configuration of system capabilities
        DAO<K, T> dao;
        try {
            dao = new FirebaseDAO(idParameterClass, typeParameterClass);
            return dao;
        } catch (FileNotFoundException e) {
            System.out.println("Firebase secret key file not found. Please check the file and try again.");
            e.printStackTrace(); 
            System.exit(1); // Terminate the application with a status code indicating abnormal termination.
        } catch (IOException e) {
            System.out.println("Error reading Firebase secret key file. Please check the file and try again.");
            e.printStackTrace();
            System.exit(1); // Terminate the application with a status code indicating abnormal termination.
        }
        // Need to return a value to satisfy the compiler, but this should never be reached.
        return null;
    }
}
