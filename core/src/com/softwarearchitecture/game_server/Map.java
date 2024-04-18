package com.softwarearchitecture.game_server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.softwarearchitecture.game_client.TexturePack;
// random integer
import java.util.Random;

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
                // return TexturePack.GRASS_01 to 04 randomly
                Random rand = new Random();
                int randInt = rand.nextInt(4) + 1;
                if (randInt == 1) {
                    return TexturePack.TILE_GRASS_01;
                } else if (randInt == 2) {
                    return TexturePack.TILE_GRASS_02;
                } else if (randInt == 3) {
                    return TexturePack.TILE_GRASS_03;
                } else {
                    return TexturePack.TILE_GRASS_04;
                }
            case PATH:
                int x = tile.getX();
                int y = tile.getY();
                int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }; // East, West, North, South
                int pathCount = 0;
                List<int[]> pathDirections = new ArrayList<>();

                for (int[] dir : directions) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    if (nx >= 0 && nx < tiles.length && ny >= 0 && ny < tiles[nx].length) {
                        Tile candidate = tiles[nx][ny];
                        if (candidate.getType() == TileType.PATH) {
                            pathCount++;
                            pathDirections.add(dir);
                        }
                    }
                }

                if (pathCount == 2) {
                    int[] dir1 = pathDirections.get(0);
                    int[] dir2 = pathDirections.get(1);
                    // Check if the path tiles are on opposite sides
                    if (dir1[0] == -dir2[0] && dir1[1] == -dir2[1]) {
                        // Path is straight
                        if (dir1[0] != 0) {
                            // Path is horizontal
                            return new Random().nextBoolean() ? TexturePack.TILE_PATH_HORIZONTAL_01
                                    : TexturePack.TILE_PATH_HORIZONTAL_02;
                        } else {
                            // Path is vertical
                            return new Random().nextBoolean() ? TexturePack.TILE_PATH_VERTICAL_01
                                    : TexturePack.TILE_PATH_VERTICAL_02;
                        }
                    } else {
                        // Determine the direction of the corner
                        if ((dir1[0] == 1 && dir2[1] == 1) || (dir2[0] == 1 && dir1[1] == 1)) {
                            return TexturePack.TILE_PATH_CORNER_02;
                        } else if ((dir1[0] == -1 && dir2[1] == 1) || (dir2[0] == -1 && dir1[1] == 1)) {
                            return TexturePack.TILE_PATH_CORNER_03;
                        } else if ((dir1[0] == -1 && dir2[1] == -1) || (dir2[0] == -1 && dir1[1] == -1)) {
                            return TexturePack.TILE_PATH_CORNER_04;
                        } else { // dir1[0] == 1 && dir2[1] == -1 || dir2[0] == 1 && dir1[1] == -1
                            return TexturePack.TILE_PATH_CORNER_01;
                        }
                    }
                } else {
                    return TexturePack.TILE_PATH_HORIZONTAL_01;
                }
            case BLOCKED_WATER:
                int x1 = tile.getX();
                int y1 = tile.getY();
                int[][] directions1 = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }; // East, West, North, South
                List<Tile> neighborTiles = new ArrayList<>();

                for (int[] dir : directions1) {
                    int nx = x1 + dir[0];
                    int ny = y1 + dir[1];
                    if (nx >= 0 && nx < tiles.length && ny >= 0 && ny < tiles[nx].length) {
                        Tile candidate = tiles[nx][ny];
                        neighborTiles.add(candidate);
                    } else {
                        // If the neighbor is outside the bounds of the map, add a BLOCKED_WATER tile
                        neighborTiles.add(new Tile(nx, ny, TileType.BLOCKED_WATER));
                    }
                }

                boolean[] isWater = new boolean[4];
                for (int i = 0; i < neighborTiles.size(); i++) {
                    if (neighborTiles.get(i).getType() == TileType.BLOCKED_WATER) {
                        isWater[i] = true;
                    } else {
                        isWater[i] = false;
                    }
                }

                // if no neighbor tiles are water
                if (!isWater[0] && !isWater[1] && !isWater[2] && !isWater[3]) {
                    return TexturePack.TILE_WATER;
                }

                // if one or three neighbor tile(s) is water
                if ((isWater[0] && !isWater[1] && !isWater[2] && !isWater[3])) {
                    return TexturePack.TILE_WATER_TOP_EDGE;
                } else if (!isWater[0] && isWater[1] && !isWater[2] && !isWater[3]) {
                    return TexturePack.TILE_WATER_RIGHT_EDGE;
                } else if (!isWater[0] && !isWater[1] && isWater[2] && !isWater[3]) {
                    return TexturePack.TILE_WATER_BOTTOM_EDGE;
                } else if (!isWater[0] && !isWater[1] && !isWater[2] && isWater[3]) {
                    return TexturePack.TILE_WATER_LEFT_EDGE;
                }

                // if two neighbor tiles are water
                if (isWater[0] && !isWater[1] && !isWater[2] && isWater[3]) {
                    return TexturePack.TILE_WATER_TOP_CORNER_LEFT;
                } else if (!isWater[0] && isWater[1] && !isWater[2] && isWater[3]) {
                    return TexturePack.TILE_WATER_TOP_CORNER_RIGHT;
                } else if (!isWater[0] && isWater[1] && isWater[2] && !isWater[3]) {
                    return TexturePack.TILE_WATER_BOTTOM_CORNER_RIGHT;
                } else if (isWater[0] && !isWater[1] && isWater[2] && !isWater[3]) {
                    return TexturePack.TILE_WATER_BOTTOM_CORNER_LEFT;
                }

                // if three neighbor tiles are water
                if (isWater[0] && isWater[1] && isWater[2] && !isWater[3]) {
                    return TexturePack.TILE_WATER_RIGHT_EDGE;
                } else if (isWater[0] && isWater[1] && !isWater[2] && isWater[3]) {
                    return TexturePack.TILE_WATER_TOP_EDGE;
                } else if (isWater[0] && !isWater[1] && isWater[2] && isWater[3]) {
                    return TexturePack.TILE_WATER_LEFT_EDGE;
                } else if (!isWater[0] && isWater[1] && isWater[2] && isWater[3]) {
                    return TexturePack.TILE_WATER_BOTTOM_EDGE;
                }

                // if all neighbor tiles are water
                if (isWater[0] && isWater[1] && isWater[2] && isWater[3]) {
                    return TexturePack.TILE_WATER;
                }

            case BLOCKED_OBSTRUCTABLE:
                // randomly choose between TILE_OBSTRUCTED_01 to 06
                Random rand1 = new Random();
                int randInt1 = rand1.nextInt(6) + 1;
                if (randInt1 == 1) {
                    return TexturePack.TILE_OBSTRUCTED_01;
                } else if (randInt1 == 2) {
                    return TexturePack.TILE_OBSTRUCTED_02;
                } else if (randInt1 == 3) {
                    return TexturePack.TILE_OBSTRUCTED_03;
                } else if (randInt1 == 4) {
                    return TexturePack.TILE_OBSTRUCTED_04;
                } else if (randInt1 == 5) {
                    return TexturePack.TILE_OBSTRUCTED_05;
                } else {
                    return TexturePack.TILE_OBSTRUCTED_06;
                }
            case BLOCKED_TREE:
                // default to TILE_TREE_BOTTOM_01 but if the tile under is tree, use
                // TILE_TREE_TOP_01
                int x2 = tile.getX();
                int y2 = tile.getY();
                if (y2 + 1 < tiles[x2].length && tiles[x2][y2 + 1].getType() == TileType.BLOCKED_TREE) {
                    return TexturePack.TILE_TREE_BOTTOM_01;
                } else {
                    return TexturePack.TILE_TREE_TOP_01;
                }
            case END:
                return TexturePack.TILE_END;
            case END_BOTTOM:
                return TexturePack.TILE_CASTLE_BOTTOM;
            case END_TOP:
                return TexturePack.TILE_CASTLE_TOP;
            case START:
                return TexturePack.TILE_PATH_HORIZONTAL_01;
            default:
                return TexturePack.COLOR_WHITE;
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
        if (!path.contains(end)) {
            path.add(end);
        }

        // System.out.println(path.size());
        return path;
    }

    private Tile findNextTile(Tile currentTile, List<Tile> pathTiles) {
        int x = currentTile.getX();
        int y = currentTile.getY();
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }; // East, West, North, South

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
