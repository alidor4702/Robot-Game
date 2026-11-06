package application;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public abstract class ConsoleItem {
    // Image to denote how the item looks in the console.
    private Image image;
    // Position of the item in the console.
    protected int posX;
    protected int posY;
    // Flag to denote if the item can move.
    private boolean canMove;

    
 // Bounds of the arena
    protected static int arenaWidth;
    protected static int arenaHeight;

    protected static GridPane consoleGrid;

    public static void setConsoleGrid(GridPane grid) {
        consoleGrid = grid;
    }
    
    // Method to set the bounds of the arena from the main application
    public static void setArenaBounds(int width, int height) {
        arenaWidth = width;
        arenaHeight = height;
    }

    // Constructor
    public ConsoleItem(String imagePath, int posX, int posY, boolean canMove) {
        this.image = new Image(imagePath);
        this.posX = posX;
        this.posY = posY;
        this.canMove = canMove;
    }
    

    public ConsoleItem(int posX, int posY, boolean canMove) {
        // Initialize position and other properties
        this.posX = posX;
        this.posY = posY;
        this.canMove = canMove;
    }


    // Getters and Setters
    public Image getImage() {
        return image;
    }

    public void setImage(String imagePath) {
        this.image = new Image(imagePath);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    // Abstract method to be implemented by subclasses for how the item should be displayed.
    public abstract void display();

    // Method to update the position if the item can move.
    public void move(int deltaX, int deltaY) {
        if (canMove) {
            int newX = posX + deltaX;
            int newY = posY + deltaY;

            // Check boundaries
            if (newX >= 0 && newX < arenaWidth && newY >= 0 && newY < arenaHeight) {
                this.posX = newX;
                this.posY = newY;
            }
            // Optionally, handle what happens if a move is attempted out of bounds
        }
    }
}
