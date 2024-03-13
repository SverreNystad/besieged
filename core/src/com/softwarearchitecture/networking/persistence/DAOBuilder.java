package com.softwarearchitecture.networking.persistence;

/**
 * A builder class for creating instances of Data Access Objects ({@link DAO}s) with configurable
 * CRUD (Create, Read, Update, Delete) capabilities. This class follows the Builder design pattern
 * to allow for configuration of DAO capabilities based on specific application needs.
 * <p>
 * The builder initializes with all CRUD operations disabled and allows enabling them individually
 * through method chaining. The final {@link DAO} instance is created with a call to {@code build()}, which
 * returns a {@link DAO} configured as per the builder settings.
 * <p>
 * Example usage:
 * <pre>
 * DAO<String, MyEntity> dao = new DAOBuilder<String, MyEntity>().withCreate()
 *                          .withRead()
 *                          .build();
 * </pre>
 * 
 * @param <T> The type of the entity object managed by the DAO.
 * @param <K> The type of the primary key used by the DAO to identify entities.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern on Wikipedia</a>
 */
public class DAOBuilder<T, K>{
    
    private boolean create = false;
    private boolean read = false;
    private boolean update = false;
    private boolean delete = false;

    /**
     * Enables the creation capability for the DAO being built.
     * 
     * @return This builder instance for method chaining.
     */
    public DAOBuilder<T, K> withCreate() {
        create = true;
        return this;
    }
    
    /**
     * Enables the read capability for the DAO being built.
     * 
     * @return This builder instance for method chaining.
     */
    public DAOBuilder<T, K> withRead() {
        read = true;
        return this;
    }
    
    /**
     * Enables the update capability for the DAO being built.
     * 
     * @return This builder instance for method chaining.
     */
    public DAOBuilder<T, K> withUpdate() {
        update = true;
        return this;
    }
    
    /**
     * Enables the delete capability for the DAO being built.
     * 
     * @return This builder instance for method chaining.
     */
    public DAOBuilder<T, K> withDelete() {
        delete = true;
        return this;
    }
    
    /**
     * Constructs and returns a DAO instance configured according to the builder settings.
     * This method decides the specific DAO implementation to instantiate based on the
     * configuration provided. Currently, defaults to {@code DAOLocal}.
     * 
     * @return A configured DAO instance.
     */
    public DAO<T, K> build() {
        // TODO: add conditional logic to determine which DAO implementation to use
        // based on the configuration of system capabilities
        DAO<T,K> dao = new DAOLocal<T,K>(create, read, update, delete);
        return dao;
    }
}
