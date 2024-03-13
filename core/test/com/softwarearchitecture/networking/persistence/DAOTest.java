package com.softwarearchitecture.networking.persistence;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DAOTest {
    
    private DAO<String, Integer> mockDAO;

    @Before
    public void setUp() {
        // dao with all capabilities
        mockDAO = new MockDAO<>(true, true, true, true);
    }

    @Test
    public void testCanCreate() {
        assertTrue("DAO should allow creation", mockDAO.canCreate());
    }

    @Test
    public void testCanRead() {
        assertTrue("DAO should allow reading", mockDAO.canRead());
    }

    @Test
    public void testCanUpdate() {
        assertTrue("DAO should allow updates", mockDAO.canUpdate());
    }

    @Test
    public void testCanDelete() {
        assertTrue("DAO should allow deletions", mockDAO.canDelete());
    }

    @Test
    public void testAddAndLoadAll() {
        String testEntity = "Test Entity";
        assertNotNull("Adding entity should return key", mockDAO.add(testEntity));
        List<String> entities = mockDAO.loadAll();
        assertNotNull("Entities list should not be null", entities);
        assertFalse("Entities list should not be empty", entities.isEmpty());
        assertTrue("Entities list should contain added entity", entities.contains(testEntity));
    }

    @Test
    public void testGet() {
        String testEntity = "Test Entity";
        Integer testId = mockDAO.add(testEntity);
        String fetchedEntity = mockDAO.get(testId);
        assertEquals("Fetched entity should match added entity", testEntity, fetchedEntity);
    }

    @Test
    public void testUpdate() {
        String testEntity = "Test Entity";
        String updatedEntity = "Updated Entity";
        Integer testId = mockDAO.add(testEntity);
        assertTrue("Update should be successful", mockDAO.update(testId, updatedEntity));
        String fetchedEntity = mockDAO.get(testId);
        assertEquals("Fetched entity should be updated", updatedEntity, fetchedEntity);
    }

    @Test
    public void testDelete() {
        String testEntity = "Test Entity";
        Integer testId = mockDAO.add(testEntity);
        assertTrue("Delete should be successful", mockDAO.delete(testId));
        assertNull("Deleted entity should not be fetchable", mockDAO.get(testId));
    }
}
