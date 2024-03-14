package com.softwarearchitecture.ecs;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ECSManagerTest {

    private ECSManager manager;

    @Before
    public void setUp() {
        manager = ECSManager.getInstance();
        manager.clearEntities();
    }

    @Test
    public void testAddEntity() {
        Entity entity = new Entity();
        int amount_of_entities = manager.getEntities().size();
        assertTrue("There should be no entities in the manager", amount_of_entities == 0);
        manager.addEntity(entity);
        amount_of_entities = manager.getEntities().size();
        assertTrue("There should be one entity in the manager", amount_of_entities == 1);

    }

    @Test
    public void testGetEntities() {
        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        manager.addEntity(entity1);
        manager.addEntity(entity2);

        int amount_of_entities = manager.getEntities().size();
        assertTrue("There should be two entities in the manager", amount_of_entities == 2);
        assertTrue("The manager should contain the first entity", manager.getEntities().contains(entity1));
        assertTrue("The manager should contain the second entity", manager.getEntities().contains(entity2));
    }

}