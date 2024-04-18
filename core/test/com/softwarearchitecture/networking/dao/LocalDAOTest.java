package com.softwarearchitecture.networking.dao;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

import com.softwarearchitecture.networking.persistence.DAO;
import com.softwarearchitecture.networking.persistence.LocalDAO;

import org.junit.Before;


import static org.junit.Assert.*;

public class LocalDAOTest {
    
     private DAO<Integer, String> mockDAO;

    @Before
    public void setUp() {
        // dao with all capabilities
        mockDAO = new LocalDAO<Integer, String>();
    }


    @Test
    public void testGet() {
        String testEntity = "Test Entity";

        Integer testId = 1;
        mockDAO.add(testId, testEntity);
        Optional<String> fetchedData = mockDAO.get(testId);
        String fetchedEntity = fetchedData.get(); 
        assertEquals("Fetched entity should match added entity", testEntity, fetchedEntity);
    }

    
    @Test
    public void testUpdate() {
        String testEntity = "Test Entity";
        String updatedEntity = "Updated Entity";
        Integer testId = 1;
        mockDAO.add(testId, testEntity);
        assertTrue("Update should be successful", mockDAO.update(testId, updatedEntity));
        Optional<String> fetchedData = mockDAO.get(testId);
        String fetchedEntity = fetchedData.get(); 
        assertEquals("Fetched entity should be updated", updatedEntity, fetchedEntity);
    }

    @Test
    public void testDelete() {
        String testEntity = "Test Entity";
        Integer testId = 1;
        mockDAO.add(testId, testEntity);
        assertTrue("Delete should be successful", mockDAO.delete(testId));
        Optional<String> fetchedData = mockDAO.get(testId);
        assertFalse("Deleted entity should not be fetchable", fetchedData.isPresent());
    }
 
}
