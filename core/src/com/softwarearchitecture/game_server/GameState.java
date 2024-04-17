package com.softwarearchitecture.game_server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.ecs.Card;
import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.CardHolderComponent;
import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TargetComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;
import com.softwarearchitecture.game_server.cards.elemental_cards.IceCard;
import com.softwarearchitecture.game_server.cards.elemental_cards.LightningCard;
import com.softwarearchitecture.game_server.cards.elemental_cards.TechnologyCard;
import com.softwarearchitecture.game_server.cards.tower_cards.BowCard;
import com.softwarearchitecture.game_server.cards.tower_cards.MagicCard;
import com.softwarearchitecture.game_server.cards.tower_cards.MeleeCard;
import com.softwarearchitecture.game_server.cards.tower_cards.PowerCard;


public class GameState implements Externalizable {

    public static final List<Class<? extends Card>> card_classes = new ArrayList<Class<? extends Card>>(
        Arrays.asList(
            IceCard.class,
            LightningCard.class,
            TechnologyCard.class,
            BowCard.class,
            MagicCard.class,
            MeleeCard.class,
            PowerCard.class
            )
    );

    public static final String game_version = "0.1";
    public Entity village = new Entity();
    public UUID gameID;
    public Entity playerOne;
    public Entity playerTwo;
    public String mapName;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // Game version
        out.writeObject(game_version);

        // Writing gameID
        out.writeObject(gameID);
        // Player entities
        out.writeObject(playerOne);
        out.writeObject(playerTwo);
        // Map
        out.writeObject(mapName);

        // Player components
        serializeComponent(out, PlayerComponent.class);
        // Position
        serializeComponent(out, PositionComponent.class);
        // Sprite
        serializeComponent(out, SpriteComponent.class);
        // Health
        serializeComponent(out, HealthComponent.class);
        // Money
        serializeComponent(out, MoneyComponent.class);
        // Velocity
        serializeComponent(out, VelocityComponent.class);
        // Cost
        serializeComponent(out, CostComponent.class);
        // Pathfinding
        serializeComponent(out, PathfindingComponent.class);
        // Tile
        serializeComponent(out, TileComponent.class);
    }

    private <T> void serializeComponent(ObjectOutput out, Class<T> componentClass) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<T> componentManager = manager.getOrDefaultComponentManager(componentClass);
        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<T> components = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            Optional<T> component = componentManager.getComponent(entity);
            if (component.isPresent()) {
                entities.add(entity);
                components.add(component.get());
            }
        }
        out.writeObject(entities);
        out.writeObject(components);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Game version
        Object readObject = in.readObject();
        if (!(readObject instanceof String)) {
            throw new IllegalStateException("Game version must be a string");
        }
        if (!game_version.equals((String) readObject)) {
            System.out.println("Game version: " + game_version + game_version.length());
            String read_game_version = (String) readObject;
            System.out.println("Read version: " + read_game_version + read_game_version.length());
            
            throw new IllegalStateException("Game version mismatch");
        }
        
        // Game ID
        readObject = in.readObject();
        if (!(readObject instanceof UUID)) {
            throw new IllegalStateException("The game id was not present");
        }
        gameID = (UUID) readObject;

        
        // Players
        deserializePlayers(in);
        // Map
        readObject = in.readObject();
        if (!(readObject instanceof String)) {
            throw new IllegalStateException("Map name must be a string");
        }
        mapName = (String) readObject;

        deserializeComponent(in, PlayerComponent.class);
        // Position
        deserializeComponent(in, PositionComponent.class);
        // Sprite
        deserializeComponent(in, SpriteComponent.class);
        // Health
        deserializeComponent(in, HealthComponent.class);
        // Money
        deserializeComponent(in, MoneyComponent.class);
        // Velocity
        deserializeComponent(in, VelocityComponent.class);
        // Cost
        deserializeComponent(in, CostComponent.class);
        // Pathfinding
        deserializeComponent(in, PathfindingComponent.class);
        // Tile
        deserializeComponent(in, TileComponent.class);
    }

    private void deserializePlayers(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        Object readObject = in.readObject();
        if (!(readObject instanceof Entity)) {
            System.out.println(readObject);
            throw new IllegalStateException("Player one must be an entity");
        }
        playerOne = (Entity) readObject;
        manager.addEntity(playerOne);
        
        // Check for player two
        readObject = in.readObject();
        if (!(readObject instanceof Entity)) {
            return;
        }
        playerTwo = (Entity) readObject;
        manager.addEntity(playerTwo);
    }
    
    private <T> void deserializeComponent(ObjectInput in, Class<T> componentClass) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<T> componentManager = manager.getOrDefaultComponentManager(componentClass);
        Object readObject = in.readObject();
        if (!(readObject instanceof List)) {
            throw new IllegalStateException("Entities must be a list");
        }
        List<?> entities = (List<?>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof List)) {
            throw new IllegalStateException("Components must be a list");
        }
        List<?> components = (List<?>) readObject;
        if (entities.size() != components.size()) {
            throw new IllegalStateException("The entity list and components must be the same size");
        }
        for (int i = 0; i < entities.size(); i++) {
            if (!(entities.get(i) instanceof Entity)) {
                throw new IllegalStateException("Entity must be an entity");
            }
            if (!(componentClass.isInstance(components.get(i)))) {
                throw new IllegalStateException("Component must be a component of class: " + componentClass.getName());
            }
            Entity entity = (Entity) entities.get(i);
            T component = componentClass.cast(components.get(i));
            componentManager.addComponent(entity, component);
            manager.addEntity(entity);
        }
    }    

    public static GameState deserializeFromByteArray(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        GameState obj = (GameState) ois.readObject();
        ois.close();
        return obj;
    }

    public static byte[] serializeToByteArray(GameState state) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(state);
        oos.close();
        return baos.toByteArray();
    }
}