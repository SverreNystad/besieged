package com.softwarearchitecture.game_server.buttons;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.softwarearchitecture.GameApp;

public class ButtonLayout {

    private int width = GameApp.WIDTH;
    private int height = GameApp.HEIGHT;
    private int gridWidth;
    private int gridHeight;
    Vector2[][] gridTable;

    public ButtonLayout(int numberOfGridsX, int numberOfGridsY, Layout layout, List<ButtonObserver> observers,
            List<ButtonType> types) {

        /*
         * Creates different layouts with buttons for each screen
         * inputs: numberOfGridsX, numberOfGridsY, layout, observers, buttontypes
         */

        this.gridWidth = width / numberOfGridsX;
        this.gridHeight = height / numberOfGridsY;

        this.gridTable = createGrids(numberOfGridsX, numberOfGridsY);

    }

    public void printGrids() {
        for (int i = 0; i < gridTable.length; i++) {
            for (int j = 0; j < gridTable[0].length; j++) {
                System.out.println(gridTable[i][j].x + " " + gridTable[i][j].y);
            }
        }
    }

    private Vector2[][] createGrids(int numberOfGridsX, int numberOfGridsY) {
        /*
         * Creates a grid of Vector2 positions
         */

        Vector2[][] gridTable = new Vector2[numberOfGridsY][numberOfGridsX];
        for (int i = 0; i < numberOfGridsY; i++) {
            for (int j = 0; j < numberOfGridsX; j++) {
                gridTable[i][j] = new Vector2(j * gridWidth, i * gridHeight);
            }
        }
        return gridTable;

    }

    private Vector2 calculateCenterGridPosition(int gridColumns, int gridRows) {
        /*
         * Calculates the center position of the grid, returns the index of the center
         * as Vector2.
         * 
         */
        int centerX = gridColumns / 2;
        int centerY = gridRows / 2;
        return new Vector2(centerX, centerY);
    }

    // main
    public static void main(String[] args) {
        ButtonLayout layout = new ButtonLayout(10, 10, Layout.MENU_SCREEN, new ArrayList<ButtonObserver>(),
                new ArrayList<ButtonType>());

    }

}
