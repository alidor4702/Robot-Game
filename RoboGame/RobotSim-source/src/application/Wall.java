package application;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Wall extends ConsoleItem {
    // Path to the image file for the wall
    private static final String WALL_IMAGE_PATH = Wall.class.getResource("/application/wall.png").toExternalForm();

    // GridPane representing the console (this would be set from the main application)
    private static GridPane consoleGrid;

    // Constructor
    public Wall(int posX, int posY) {
        super(WALL_IMAGE_PATH, posX, posY, false); // Walls do not move
    }

    // Method to set the console grid from the main application
    public static void setConsoleGrid(GridPane grid) {
        consoleGrid = grid;
    }

    @Override
    public void display() {
        ImageView robotView = new ImageView(getImage());
        
        // Remove old view if it exists
        consoleGrid.getChildren().removeIf(node -> 
            GridPane.getRowIndex(node) == getPosY() && GridPane.getColumnIndex(node) == getPosX()
        );

        // Add the ImageView to the console grid at the new position
        GridPane.setConstraints(robotView, getPosX(), getPosY());
        consoleGrid.getChildren().add(robotView);
    }


    @Override
    public void move(int deltaX, int deltaY) {
        // Walls are immovable, so this method is overridden to do nothing.
    }
}
