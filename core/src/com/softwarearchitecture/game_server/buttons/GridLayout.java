package com.softwarearchitecture.game_server.buttons;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.GameApp;
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
        return 1f / columns * GameApp.WIDTH;
    }

    public float getGridHeight() {
        return 1f / rows * GameApp.HEIGHT * 0.8f; // magic number to make the grid smaller, can be calibrated
    }

    public Vector2 getGridPosition(int columnNumber, int rowNumber) {
        return new Vector2((float) rowNumber / rows, (float) columnNumber / columns);
    }

    /**
     * Finds the psositions the butons can be aligned in the center of the screen
     * Parameters: numberOfButtons, rowNumber (where on the screen the buttons
     * should be placed)
     * Returns: List of Vector2 positions for the buttons
     */
    public List<Vector2> alignCenterX(int numberOfButtons, int rowNumber) {

        List<Vector2> buttonPositions = new ArrayList<>();
        float gridWidth = getGridWidth();
        float startX = (columns - numberOfButtons) * gridWidth * 0.5f; // Starting X position to center buttons
        float y = getGridPosition(0, rowNumber).y; // Y position is constant for all buttons in the row

        for (int i = 0; i < numberOfButtons; i++) {
            float x = startX + i * gridWidth; // Calculate X position for each button

            buttonPositions.add(new Vector2(x, y));
        }

        return buttonPositions;
    }

    public List<Vector2> alignCenterY(int numberOfButtons, int columnNumber) {
        List<Vector2> buttonPositions = new ArrayList<>();
        float gridHeight = getGridHeight();
        float startY = (rows - numberOfButtons) * gridHeight * 0.5f; // Starting Y position to center buttons
        float x = getGridPosition(columnNumber, 0).x; // X position is constant for all buttons in the column

        for (int i = 0; i < numberOfButtons; i++) {
            float y = startY + i * gridHeight; // Calculate Y position for each button

            buttonPositions.add(new Vector2(x, y));
        }

        return buttonPositions;
    }

    /**
     * Calculate new positions with padding and creating a new rectangle for the
     * button
     */
    public Rectangle createPadding(Vector2 pos, float padding) {
        float paddedX = pos.x + (getGridWidth() * padding) - (getGridWidth() / 2f * padding);
        float paddedY = pos.y + (getGridHeight() * padding) - (getGridHeight() / 2f * padding);
        float paddedWidth = getGridWidth() * (1 - padding * 2);
        float paddedHeight = getGridHeight() * (1 - padding * 2);
        return new Rectangle(paddedX, paddedY, paddedWidth, paddedHeight);

    }

    /**
     * Creates a list of rectangles for the buttons to be placed vertically
     */
    public List<Rectangle> getButtonsVertically(int numberOfButtons) {
        Vector2 center = findCenterGrid();
        int columnNumber = (int) center.x;
        List<Rectangle> buttonRectangles = new ArrayList<>();
        List<Vector2> buttonPositions = alignCenterY(numberOfButtons, columnNumber);
        for (Vector2 pos : buttonPositions) {
            buttonRectangles.add(createPadding(pos, (float) 0.1));
        }
        return buttonRectangles;
    }

    /**
     * Creates a list of rectangles for the buttons to be placed horizontally
     */
    public List<Rectangle> getButtonsHorizontally(int numberOfButtons) {
        int rowNumber = (int) findCenterGrid().y;
        List<Rectangle> buttonRectangles = new ArrayList<>();
        List<Vector2> buttonPositions = alignCenterX(numberOfButtons, rowNumber);
        for (Vector2 pos : buttonPositions) {
            buttonRectangles.add(createPadding(pos, (float) 0.1));
        }
        return buttonRectangles;
    }

    public static void main(String[] args) {
        int buttons = 3;
        int rows = 5;
        int columns = 5;
        GridLayout grid = new GridLayout(columns, rows);

        List<Vector2> buttonPositions = grid.alignCenterX(buttons, 5);
        for (Vector2 pos : buttonPositions) {
            pos = grid.createPadding(pos, (float) 0.1).getPosition();
        }

    }

}
