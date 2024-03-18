package com.softwarearchitecture.game_server.buttons;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

public class GridLayout {

    private int columns;
    private int rows;

    public GridLayout(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public int getNumberOfColumns() {
        return columns;
    }

    public int getNumberOfRows() {
        return rows;
    }

    public Vector2 findCenter() {
        return new Vector2(0.5f, 0.5f);
    }

    public Vector2 findCenterGrid() {
        return new Vector2((float) columns / 2, (float) rows / 2);
    }

    public float getGridWidth() {
        return 1f / columns;
    }

    public float getGridHeight() {
        return 1f / rows;
    }

    public Vector2 getGridPosition(int columnNumber, int rowNumber) {
        return new Vector2((float) rowNumber / rows, (float) columnNumber / columns);
    }

    // public List<Vector2> alignCenterX(int numberOfButtons, int rowNumber) {
    // List<Vector2> buttons = new ArrayList();
    // float y = getGridPosition(0, rowNumber).y;
    // float centerX = findCenterGrid().x; // burde kanskje bruke denne
    // for (int i = numberOfButtons / 2; i < numberOfButtons; i++) {
    // float x = getGridPosition(i, rowNumber).x;
    // buttons.add(new Vector2(x, y));
    // }
    // return buttons;

    // }

    public List<Vector2> alignCenterX(int numberOfButtons, int rowNumber) {
        List<Vector2> buttons = new ArrayList<>();
        float gridWidth = getGridWidth();
        float startX = (columns - numberOfButtons) * gridWidth * 0.5f; // Starting X position to center buttons
        float y = getGridPosition(0, rowNumber).y; // Y position is constant for all buttons in the row

        for (int i = 0; i < numberOfButtons; i++) {
            float x = startX + i * gridWidth; // Calculate X position for each button
            buttons.add(new Vector2(x, y));
        }

        return buttons;
    }

    public Rectangle createPadding(Vector2 pos, float padding) {
        /*
         * Calculate new positions with padding
         **/
        float paddedX = pos.x + (getGridWidth() * padding) - (getGridWidth() / 2f * padding);
        float paddedY = pos.y + (getGridHeight() * padding) - (getGridHeight() / 2f * padding);
        float paddedWidth = getGridWidth() * (1 - padding * 2);
        float paddedHeight = getGridHeight() * (1 - padding * 2);
        return new Rectangle(paddedX, paddedY, paddedWidth, paddedHeight);

    }

    public static void main(String[] args) {
        int buttons = 3;
        int rows = 5;
        int columns = 5;
        GridLayout grid = new GridLayout(columns, rows);

        List<Vector2> buttonPositions = grid.alignCenterX(buttons, 5);
        for (Vector2 pos : buttonPositions) {
            pos = grid.createPadding(pos, (float) 0.1).getPosition();
            System.out.println(pos.x + " " + pos.y);
        }

    }

}
