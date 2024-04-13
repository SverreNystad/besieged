package com.softwarearchitecture.ecs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TileType;

public class TileComponentManager extends ComponentManager<TileComponent> {
    private Map<String, TileComponent> tileMap = new HashMap<>();

    public TileComponentManager() {
        // super();
    }

    @Override
    public void addComponent(Entity entity, TileComponent component) {
        // super.addComponent(entity, component);
        // Tile tile = component.getTile();
        // Indexing tiles by their coordinates
        // tileMap.put(tileKey(tile.getX(), tile.getY()), component);
    }

    public Tile getTileAt(int x, int y) {
        TileComponent tileComponent = tileMap.get(tileKey(x, y));
        return tileComponent != null ? tileComponent.getTile() : null;
    }

    private String tileKey(int x, int y) {
        return x + "," + y; // Keys are "x,y"
    }

    public List<Tile> getTilesOfType(TileType type) {
        return components.values().stream()
            .map(TileComponent::getTile)
            .filter(t -> t.getType() == type)
            .collect(Collectors.toList());
    }

    public Tile getStartTile() {
        return getTilesOfType(TileType.START).stream().findFirst().orElse(null);
    }

    public Tile getEndTile() {
        return getTilesOfType(TileType.END).stream().findFirst().orElse(null);
    }

    public List<Tile> getPathTiles() {
        return getTilesOfType(TileType.PATH);
    }
}
