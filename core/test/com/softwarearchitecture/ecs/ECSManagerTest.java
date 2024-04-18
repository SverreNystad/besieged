package com.softwarearchitecture.ecs;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ECSManagerTest {

    private ECSManager manager;

    @Before
    public void setUp() {
        manager = ECSManager.getInstance();
        manager.clearLocalEntities();
    }

    @Test
    public void testAddEntity() {
        Entity entity = new Entity();
        int amount_of_entities = manager.getLocalEntities().size();
        assertTrue("There should be no entities in the manager", amount_of_entities == 0);
        manager.addLocalEntity(entity);
        manager.update(0);
        amount_of_entities = manager.getLocalEntities().size();
        assertTrue("There should be one entity in the manager", amount_of_entities == 1);

    }

    @Test
    public void testGetEntities() {
        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        manager.addLocalEntity(entity1);
        manager.addLocalEntity(entity2);
        manager.update(0);
        int amount_of_entities = manager.getLocalEntities().size();
        assertTrue("There should be two entities in the manager", amount_of_entities == 2);
        assertTrue("The manager should contain the first entity", manager.getLocalEntities().contains(entity1));
        assertTrue("The manager should contain the second entity", manager.getLocalEntities().contains(entity2));
    }
    
    @Test
    public void testAddingOnlyUniqueEntities() {
        Entity entity1 = new Entity();
        Entity entity2 = new Entity();
        manager.addLocalEntity(entity1);
        manager.addLocalEntity(entity2);
        manager.addLocalEntity(entity1);
        manager.update(0);
        int amount_of_entities = manager.getLocalEntities().size();
        assertTrue("There should be two entities in the manager", amount_of_entities == 2);
    }

}