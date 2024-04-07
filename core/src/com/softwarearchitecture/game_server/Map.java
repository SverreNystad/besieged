package com.softwarearchitecture.game_server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Map {
    protected Tile[][] tiles;
    protected String backgroundImage;
    public Texture placeableTexture = new Texture(Gdx.files.internal("grass.png"));
    public Texture pathTexture = new Texture(Gdx.files.internal("road.jpg"));
    public Texture defaultTexture = new Texture(Gdx.files.internal("chad.jpg"));


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

    public Texture getTextureForTile(Tile tile) {
        switch (tile.getType()) {
            case PLACEABLE:
                return placeableTexture;
            case PATH:
                return pathTexture;
            default:
                return defaultTexture; 
        }
    }
}
