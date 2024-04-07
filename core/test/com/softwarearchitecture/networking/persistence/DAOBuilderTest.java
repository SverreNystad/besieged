package com.softwarearchitecture.networking.persistence;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DAOBuilderTest {

    private DAOBuilder<String, String> builder;
    private DAO<String, String> dao;

    @Before
    public void setUp() {
        builder = new DAOBuilder<>();
    }
    
    @Test
    public void testBuildsWithCreate() {
        dao = builder.withCreate().build(String.class, String.class);
        assertNotNull(dao);
        
        // Verify dao has create capability 
        assertTrue(dao.canCreate());
        assertFalse(dao.canRead());
        assertFalse(dao.canUpdate());
        assertFalse(dao.canDelete());
    }


    @Test
    public void testBuildsWithRead() {
        dao = builder.withRead().build(String.class, String.class);
        assertNotNull(dao);
           
        // Verify dao has create capability 
        assertFalse(dao.canCreate());
        assertTrue(dao.canRead());
        assertFalse(dao.canUpdate());
        assertFalse(dao.canDelete());
    }

    @Test
    public void testBuildsWithUpdate() {
        dao = builder.withUpdate().build(String.class, String.class);
        assertNotNull(dao);
        
        // Verify dao has create capability 
        assertFalse(dao.canCreate());
        assertFalse(dao.canRead());
        assertTrue(dao.canUpdate());
        assertFalse(dao.canDelete());
    }

    @Test
    public void testBuildsWithDelete() {
        dao = builder.withDelete().build(String.class, String.class);
        assertNotNull(dao);
        
        // Verify dao has create capability 
        assertFalse(dao.canCreate());
        assertFalse(dao.canRead());
        assertFalse(dao.canUpdate());
        assertTrue(dao.canDelete());
    }

    @Test
    public void testBuildsWithAllCapabilities() {
        dao = builder.withCreate().withRead().withUpdate().withDelete().build(String.class, String.class);;
        assertNotNull(dao);
        
        // Verify dao has create capability 
        assertTrue(dao.canCreate());
        assertTrue(dao.canRead());
        assertTrue(dao.canUpdate());
        assertTrue(dao.canDelete());
    }

}
