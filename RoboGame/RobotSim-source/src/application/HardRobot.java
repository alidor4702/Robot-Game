package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HardRobot {
    private ImageView robotView;
    private double x, y;
    private double moveRange; // The range the robot can move
    private Direction movementDirection;
    private double moveSpeed = 2;
    private enum Direction { HORIZONTAL, VERTICAL }
    private RobotApplication app; 

    public HardRobot(RobotApplication app, String imagePath, double x, double y, double moveRange, Direction direction) {
        Image robotImage = new Image(imagePath);
        this.app = app;
        robotView = new ImageView(robotImage);
        robotView.setFitHeight(20); // Adjust the size as needed
        robotView.setFitWidth(20);
        this.x = x;
        this.y = y;
        this.moveRange = moveRange;
        this.movementDirection = direction;
        robotView.setX(x);
        robotView.setY(y);
    }

    public void move() {
        // Example movement logic, adjust as needed
        switch (movementDirection) {
            case HORIZONTAL:
                x += moveSpeed;
                if (x > moveRange) moveSpeed = -moveSpeed; // Reverse direction
                if (x < 0) moveSpeed = Math.abs(moveSpeed); // Reverse direction
                break;
            case VERTICAL:
                y += moveSpeed;
                if (y > moveRange) moveSpeed = -moveSpeed; // Reverse direction
                if (y < 0) moveSpeed = Math.abs(moveSpeed); // Reverse direction
                break;
        }
        // Update position with boundary checks
        robotView.setX(Math.min(Math.max(0, x), app.getRootWidth() - robotView.getFitWidth()));
        robotView.setY(Math.min(Math.max(0, y), app.getRootHeight() - robotView.getFitHeight()));
    }

    public ImageView getRobotView() {
        return robotView;
    }
}
