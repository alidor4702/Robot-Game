package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;


public class SoftRobot extends ConsoleItem {
    private Circle robotCircle;
    private int robotNumber;
    enum Direction { DOWN, RIGHT, UP, LEFT }
    Direction currentDirection = Direction.DOWN;

    // Constructor
    public SoftRobot(int posX, int posY, int robotNumber) {
        super(posX, posY, true); // The third argument is movable
        this.robotNumber = robotNumber;

        // Initialize the Circle with the robot's starting position and size
        robotCircle = new Circle(5); // Radius of 5
        robotCircle.setFill(Color.BLUE); // Set color
        GridPane.setConstraints(robotCircle, posX, posY);
        consoleGrid.getChildren().add(robotCircle);
    }

//    public void setPrevPosX(int x) {
//        this.prevPosX = x;
//    }
//
//    public void setPrevPosY(int y) {
//        this.prevPosY = y;
//    }
    // Method to set the console grid from the main application
    public static void setConsoleGrid(GridPane grid) {
        consoleGrid = grid;
    }

    public void bounceOff(SoftRobot other) {
        // This method is called when this SoftRobot collides with another SoftRobot
        // Reverse the direction of movement for both robots to simulate a bounce
        int deltaX = getPosX() - other.getPosX();
        int deltaY = getPosY() - other.getPosY();

        // Adjust position to ensure the robots bounce off in opposite directions
        move(deltaX, deltaY); // Move this robot away from the collision
        other.move(-deltaX, -deltaY); // Move the other robot in the opposite direction
        
        // After moving, ensure that neither robot has moved out of bounds.
        // If they have, you would need to correct their position here.
        if (getPosX() < 0) setPosX(0);
        if (getPosY() < 0) setPosY(0);
        if (getPosX() >= arenaWidth) setPosX(arenaWidth - 1);
        if (getPosY() >= arenaHeight) setPosY(arenaHeight - 1);

        // Repeat boundary correction for the other SoftRobot
        if (other.getPosX() < 0) other.setPosX(0);
        if (other.getPosY() < 0) other.setPosY(0);
        if (other.getPosX() >= arenaWidth) other.setPosX(arenaWidth - 1);
        if (other.getPosY() >= arenaHeight) other.setPosY(arenaHeight - 1);

        // ... (rest of the bounceOff method)
        // Update display after bouncing
        display();
        other.display();
        
        
    }
    
    @Override
    public void display() {
        GridPane.setConstraints(robotCircle, getPosX(), getPosY());
        if (!consoleGrid.getChildren().contains(robotCircle)) {
            consoleGrid.getChildren().add(robotCircle);
        }
    }

    // Method to move the robot randomly
    public void moveRandomly() {
        Random rand = new Random();
        // Generate random deltas for X and Y (for example, -1, 0, or 1)
        int deltaX = rand.nextInt(3) - 1;
        int deltaY = rand.nextInt(3) - 1;
        
        // Move the robot by the random deltas
     // Calculate new position and ensure it's within the arena bounds
        int newX = Math.max(0, Math.min(getPosX() + deltaX, ConsoleItem.arenaWidth - 1));
        int newY = Math.max(0, Math.min(getPosY() + deltaY, ConsoleItem.arenaHeight - 1));

        // Update position
        setPosX(newX);
        setPosY(newY);
        //this.display();
    }

    
    

    public void moveOneStep() {
        int deltaX = 0, deltaY = 0;
        switch (currentDirection) {
            case UP:
                deltaY = -1;
                break;
            case DOWN:
                deltaY = 1;
                break;
            case LEFT:
                deltaX = -1;
                break;
            case RIGHT:
                deltaX = 1;
                break;
        }

        // Check if the next move stays within the boundary
        int newX = getPosX() + (currentDirection == Direction.RIGHT ? 1 : currentDirection == Direction.LEFT ? -1 : 0);
        int newY = getPosY() + (currentDirection == Direction.DOWN ? 1 : currentDirection == Direction.UP ? -1 : 0);

        if (newX >= 0 && newX < ConsoleItem.arenaWidth && newY >= 0 && newY < ConsoleItem.arenaHeight) {
            setPosX(newX);
            setPosY(newY);
        }
        System.out.println("Moving to: " + getPosX() + ", " + getPosY());
    }

    public void changeDirection(Direction newDirection) {
        currentDirection = newDirection;
    }

    
    
    
 // Method to handle collision with walls

    
    private int lastDeltaX = 0;
    private int lastDeltaY = 0;

    // Constructor and other members...

    @Override
    public void move(int deltaX, int deltaY) {
        super.move(deltaX, deltaY); // Call the base class move method
        this.lastDeltaX = deltaX;
        this.lastDeltaY = deltaY;
    }

    // Method to handle collision with walls
    public void bounceOffWall() {
        // Reverse the direction of movement
        int newDeltaX = -lastDeltaX;
        int newDeltaY = -lastDeltaY;

        // Move the robot in the new direction
        move(newDeltaX, newDeltaY);

        // Update display after bouncing
        display();
    }
    // Method to handle collision with walls
    public void deleteDueToCollision() {
        // This method should be called by the collision detection system
        // when the soft robot collides with a hard robot. It handles the
        // removal of the soft robot from the console grid.
        consoleGrid.getChildren().removeIf(node ->
            GridPane.getColumnIndex(node) == getPosX() && GridPane.getRowIndex(node) == getPosY()
            && "Robot ".concat(String.valueOf(robotNumber)).equals(node.getAccessibleText())
        );
    }
}
