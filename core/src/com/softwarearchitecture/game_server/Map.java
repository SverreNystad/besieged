package com.softwarearchitecture.game_server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Map {
    protected Tile[][] tiles;
    protected String backgroundImage;
    private float tileHeight;
    private float tileWidth;

    public Map(String mapString, String backgroundImage) {
        this.backgroundImage = backgroundImage;
        int rows = mapString.split(";").length;
        int cols = mapString.split(";")[0].split(",\\s*").length;
        tiles = new Tile[rows][cols];
        populateMapFromMapString(mapString, rows, cols);
    }

    private void populateMapFromMapString(String mapString, int rows, int cols) {
        String[] rowsArray = mapString.split(";");
        for (int i = 0; i < rows; i++) {
            String[] colsArray = rowsArray[i].split(",\\s*");
            for (int j = 0; j < cols; j++) {
                TileType type = TileType.valueOf(colsArray[j].trim().toUpperCase());
                tiles[i][j] = new Tile(i, j, type);
            }
        }
    }

    public Tile[][] getMapLayout() {
        return tiles;
    }

    public boolean isBuildable(int row, int col) {
        return tiles[row][col].getType() == TileType.PLACEABLE;
    }

    public String getTextureForTile(Tile tile) {
        switch (tile.getType()) {
            case PLACEABLE:
                return TexturePack.placeableTexture;
            case PATH:
                return TexturePack.pathTexture;
            case BLOCKED_WATER:
                return TexturePack.waterTexture;
            case BLOCKED_ROCK:
                return TexturePack.rockTexture;
            case BLOCKED_TREE:
                return TexturePack.treeTexture;
            default:
                return TexturePack.defaultTexture; 
        }
    }

    // Getters and setters for tileWidth and tileHeight
    public float getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(float tileHeight) {
        this.tileHeight = tileHeight;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(float tileWidth) {
        this.tileWidth = tileWidth;
    }

    public List<Tile> getPath() {
        Tile start = null;
        Tile end = null;
        List<Tile> pathTiles = new ArrayList<>();

        // First, identify start, end, and path tiles
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                if (tile.getType() == TileType.START) {
                    start = tile;
                } else if (tile.getType() == TileType.END) {
                    end = tile;
                } else if (tile.getType() == TileType.PATH) {
                    pathTiles.add(tile);
                }
            }
        }

        if (start == null || end == null) {
            return new ArrayList<>(); // No valid path if start or end is missing
        }

        // Build the path starting from the start tile
        List<Tile> path = new LinkedList<Tile>();
        Tile currentTile = start;
        path.add(start);

        while (currentTile != end && !pathTiles.isEmpty()) {
            Tile nextTile = findNextTile(currentTile, pathTiles);
            if (nextTile != null) {
                path.add(nextTile);
                pathTiles.remove(nextTile);
                currentTile = nextTile;
            } else {
                break; // No more valid paths
            }
        }
        if(!path.contains(end)){
            path.add(end);
        }

        
        System.out.println(path.size());
        return path;
    }

    private Tile findNextTile(Tile currentTile, List<Tile> pathTiles) {
        int x = currentTile.getX();
        int y = currentTile.getY();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // East, West, North, South

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < tiles.length && ny >= 0 && ny < tiles[nx].length) {
                Tile candidate = tiles[nx][ny];
                if (pathTiles.contains(candidate) || candidate.getType() == TileType.END) {
                    return candidate;
                }
            }
        }
        return null; // No adjacent path tiles found
    }

    public int getMapWidth() {
        return tiles[0].length;
    }

    public int getMapHeight() {
        return tiles.length;
    }
}
