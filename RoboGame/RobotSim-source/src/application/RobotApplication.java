package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


public class RobotApplication extends Application {
	enum PlacementMode { NONE, WALL, HARD_ROBOT }
	
	private PlacementMode currentPlacementMode = PlacementMode.NONE;
	private Pane root;
    private ImageView robotView;
    private ImageView cakeView;
    private List<ImageView> walls;
    private List<HardRobot> hardRobots; // List to hold HardRobots
    private double robotX = 100, robotY = 100;  // Initial position
    private double robotSpeed = 5;
    enum Direction { UP, DOWN, LEFT, RIGHT, NONE, VERTICAL, HORIZONTAL }
    Direction currentDirection = Direction.NONE;
    private boolean isSetupMode = true;
 
    
    //RobotApplication.class.getResource("/application/wall.png").toExternalForm()
    private List<ImageView> createWalls() {
        List<ImageView> walls = new ArrayList<>();
        Image wallImage = new Image(RobotApplication.class.getResource("/application/wall.png").toExternalForm()
); // Update path

        // Define the width of each wall segment and the gap size
        double wallWidth = 100; // Width of each wall segment
        double wallHeight = 20; // Height of the wall
        double gapSize = 100;   // Size of the opening in the middle
        double arenaWidth = 400; // Width of the arena - adjust to your scene's width
        double arenaHeight = 400; // Height of the arena - adjust to your scene's height

        // Calculate the middle Y position
        double middleY = (arenaHeight - wallHeight) / 2; // Center on the Y-axis

        // Create left wall segment
        ImageView leftWallSegment = new ImageView(wallImage);
        leftWallSegment.setFitWidth((arenaWidth - gapSize) / 2);
        leftWallSegment.setFitHeight(wallHeight);
        leftWallSegment.setX(0); // Start at the left edge
        leftWallSegment.setY(middleY); // Center on the Y-axis
        walls.add(leftWallSegment);

        // Create right wall segment
        ImageView rightWallSegment = new ImageView(wallImage);
        rightWallSegment.setFitWidth((arenaWidth - gapSize) / 2);
        rightWallSegment.setFitHeight(wallHeight);
        rightWallSegment.setX((arenaWidth + gapSize) / 2); // Start after the gap
        rightWallSegment.setY(middleY); // Center on the Y-axis
        walls.add(rightWallSegment);

        return walls;
    }

    private boolean isCollidingWithCake() {
        return robotX < cakeView.getX() + cakeView.getFitWidth() && 
               robotX + robotView.getFitWidth() > cakeView.getX() &&
               robotY < cakeView.getY() + cakeView.getFitHeight() && 
               robotY + robotView.getFitHeight() > cakeView.getY();
    }


    private MenuBar createMenu(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        MenuItem startGame = new MenuItem("Start Game");
        MenuItem placeWalls = new MenuItem("Place Walls");
        MenuItem placeHardRobots = new MenuItem("Place Hard Robots");

        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Level");
        MenuItem loadItem = new MenuItem("Load Level");

        saveItem.setOnAction(e -> saveLevel("level.txt")); // Implement saveLevel method
        loadItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                loadLevel(file.getAbsolutePath()); // Implement loadLevel method
            }
        });

        fileMenu.getItems().addAll(saveItem, loadItem);
        menuBar.getMenus().add(fileMenu);


        
        startGame.setOnAction(e -> {
            isSetupMode = false; // Switch off setup mode
            startAnimation(); // Start the game
        });

        placeWalls.setOnAction(e -> currentPlacementMode = PlacementMode.WALL);
        placeHardRobots.setOnAction(e -> currentPlacementMode = PlacementMode.HARD_ROBOT);

        gameMenu.getItems().addAll(startGame, placeWalls, placeHardRobots);
        menuBar.getMenus().add(gameMenu);
        return menuBar;
    }

    private void saveLevel(String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            for (ImageView wall : walls) {
                out.println("wall," + wall.getX() + "," + wall.getY());
            }
            for (HardRobot hr : hardRobots) {
                out.println("hardrobot," + hr.getX() + "," + hr.getY());
            }
            out.println("cake," + cakeView.getX() + "," + cakeView.getY());
            out.println("softrobot," + robotView.getX() + "," + robotView.getY());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void loadLevel(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            // Clear existing objects
            root.getChildren().removeAll(walls);
            walls.clear();
            root.getChildren().removeIf(node -> hardRobots.contains(node));
            hardRobots.clear();

            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                String type = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);

                switch (type) {
                    case "wall":
                        ImageView wall = createWall(x, y);
                        walls.add(wall);
                        root.getChildren().add(wall);
                        break;
                    case "hardrobot":
                        HardRobot hardRobot = createHardRobot(x, y);
                        hardRobots.add(hardRobot);
                        root.getChildren().add(hardRobot.getRobotView());
                        break;
                    case "cake":
                        cakeView.setX(x);
                        cakeView.setY(y);
                        break;
                    case "softrobot":
                        robotView.setX(x);
                        robotView.setY(y);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, 400, 400);

        // Add menu
        MenuBar menuBar = createMenu(primaryStage);
        root.getChildren().add(menuBar);

        // Mouse click event for placing walls
        scene.setOnMouseClicked(event -> {
            if (isSetupMode) {
                switch (currentPlacementMode) {
                    case WALL:
                        ImageView newWall = createWall(event.getX(), event.getY());
                        walls.add(newWall);
                        root.getChildren().add(newWall);
                        break;
                    case HARD_ROBOT:
                        HardRobot newRobot = createHardRobot(event.getX(), event.getY());
                        hardRobots.add(newRobot);
                        root.getChildren().add(newRobot.getRobotView());
                        break;
                    default:
                        break;
                }
            }
        });
        // Initialize the ImageView with the robot image
        Image robotImage = new Image(RobotApplication.class.getResource("/application/soft.png").toExternalForm());
        robotView = new ImageView(robotImage);
        robotView.setX(robotX);
        robotView.setY(robotY);
        robotView.setFitHeight(20);
        robotView.setFitWidth(20);
        root.getChildren().add(robotView); // Add robotView here
        
        Image cakeImage = new Image(RobotApplication.class.getResource("/application/cake.png").toExternalForm());
        cakeView = new ImageView(cakeImage);
        cakeView.setFitHeight(20); // Adjust size as needed
        cakeView.setFitWidth(20);
        cakeView.setX(200);
        cakeView.setY(300);
        root.getChildren().add(cakeView);

        walls = createWalls();
        for (ImageView wall : walls) {
            root.getChildren().add(wall); // Add each wall here
        }

        // Initialize HardRobots
        hardRobots = new ArrayList<>();
        hardRobots.add(new HardRobot(RobotApplication.class.getResource("/application/hard.png").toExternalForm(), 50, 50, 50, Direction.HORIZONTAL));
        hardRobots.add(new HardRobot(RobotApplication.class.getResource("/application/hard.png").toExternalForm(), 350, 50, 50, Direction.VERTICAL));
        // Do not add hard robot's ImageView here, it's already added in the constructor of HardRobot

        setUpKeyHandling(scene);
        startAnimation(); // Start the game animation

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HardRobot createHardRobot(double x, double y) {
        // Create a new HardRobot
        HardRobot newRobot = new HardRobot(RobotApplication.class.getResource("/application/hard.png").toExternalForm(), x, y, 50, Direction.HORIZONTAL);
        // Add its ImageView to the root only here
        if (!root.getChildren().contains(newRobot.getRobotView())) {
            root.getChildren().add(newRobot.getRobotView());
        }
        return newRobot;
    }


    
    // Getter method for the dimensions of the root pane
    public double getRootWidth() {
        return root.getWidth();
    }

    public double getRootHeight() {
        return root.getHeight();
    }
    
    private boolean isCollidingWithWalls(double nextX, double nextY) {
        for (ImageView wall : walls) {
            if (nextX < wall.getX() + wall.getFitWidth() && nextX + robotView.getFitWidth() > wall.getX() &&
                nextY < wall.getY() + wall.getFitHeight() && nextY + robotView.getFitHeight() > wall.getY()) {
                return true; // Collision detected
            }
        }
        return false; // No collision
    }

    private ImageView createWall(double x, double y) {
        Image wallImage = new Image(RobotApplication.class.getResource("/application/wall.png").toExternalForm());
        ImageView wall = new ImageView(wallImage);
        wall.setFitWidth(20); // Adjust size as needed
        wall.setFitHeight(20);
        wall.setX(x);
        wall.setY(y);
        return wall;
    }


    private void setUpKeyHandling(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    currentDirection = Direction.UP;
                    break;
                case DOWN:
                    currentDirection = Direction.DOWN;
                    break;
                case LEFT:
                    currentDirection = Direction.LEFT;
                    break;
                case RIGHT:
                    currentDirection = Direction.RIGHT;
                    break;
                default:
                    break;
            }
        });

        scene.setOnKeyReleased(event -> currentDirection = Direction.NONE);
    }

    private void moveRobot() {
        double nextX = robotX;
        double nextY = robotY;
        double robotWidth = robotView.getFitWidth();
        double robotHeight = robotView.getFitHeight();

        switch (currentDirection) {
            case UP:
                nextY = Math.max(nextY - robotSpeed, 0); // Prevent moving above the top boundary
                break;
            case DOWN:
                nextY = Math.min(nextY + robotSpeed, root.getHeight() - robotHeight); // Prevent moving below the bottom boundary
                break;
            case LEFT:
                nextX = Math.max(nextX - robotSpeed, 0); // Prevent moving past the left boundary
                break;
            case RIGHT:
                nextX = Math.min(nextX + robotSpeed, root.getWidth() - robotWidth); // Prevent moving past the right boundary
                break;
            default:
                break; // Do nothing if no direction is pressed
        }

        // Check for collisions with walls only if we're within the boundaries
        if (!isCollidingWithWalls(nextX, nextY)) {
            robotX = nextX;
            robotY = nextY;
            robotView.setX(robotX);
            robotView.setY(robotY);
        }
    }

    public class HardRobot {
        private ImageView robotView;
        private double x, y;
        private final double originalX, originalY;
        private double moveRange; // The range the robot can move
        private Direction movementDirection;
        private double moveSpeed = 2;

        public HardRobot(String imagePath, double x, double y, double moveRange, Direction direction) {
            Image robotImage = new Image(imagePath);
            robotView = new ImageView(robotImage);
            robotView.setFitHeight(20); // Adjust the size as needed
            robotView.setFitWidth(20);
            this.x = x;
            this.y = y;
            this.originalX = x;
            this.originalY = y;
            this.moveRange = moveRange;
            this.movementDirection = direction;
            robotView.setX(x);
            robotView.setY(y);
            root.getChildren().add(robotView);
        }
        
        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
        
        

        public void move(double rootWidth, double rootHeight) {
            // Movement logic for horizontal direction
            if (movementDirection == Direction.HORIZONTAL) {
                x += moveSpeed;
                if (x < originalX || x > originalX + moveRange) {
                    moveSpeed = -moveSpeed; // Reverse direction
                    x += moveSpeed; // Apply the movement after reversing the direction
                }
            }

            // Movement logic for vertical direction
            if (movementDirection == Direction.VERTICAL) {
                y += moveSpeed;
                if (y < originalY || y > originalY + moveRange) {
                    moveSpeed = -moveSpeed; // Reverse direction
                    y += moveSpeed; // Apply the movement after reversing the direction
                }
            }

            // Update the ImageView position with clamping to prevent going out of bounds
            robotView.setX(Math.max(0, Math.min(x, rootWidth - robotView.getFitWidth())));
            robotView.setY(Math.max(0, Math.min(y, rootHeight - robotView.getFitHeight())));
        }

        public ImageView getRobotView() {
            return robotView;
        }
    }
    
    private boolean isCollidingWithHardRobots() {
        for (HardRobot hr : hardRobots) {
            ImageView hrView = hr.getRobotView();
            if (robotX < hrView.getX() + hrView.getFitWidth() && robotX + robotView.getFitWidth() > hrView.getX() &&
                robotY < hrView.getY() + hrView.getFitHeight() && robotY + robotView.getFitHeight() > hrView.getY()) {
                return true; // Collision detected
            }
        }
        return false; // No collision
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            ButtonType buttonTypeRetry = new ButtonType("Retry");
            ButtonType buttonTypeEnd = new ButtonType("End Program");

            alert.getButtonTypes().setAll(buttonTypeRetry, buttonTypeEnd);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeRetry) {
                restartGame();
            } else {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close(); // Close the application
            }
        });
    }


    private void restartGame() {
        // Reset the game state (reset the position of the soft robot, etc.)
        robotX = 100;
        robotY = 100;
        robotView.setX(robotX);
        robotView.setY(robotY);
        currentDirection = Direction.NONE;

        // Restart the animation timer
        startAnimation();
    }

    private void startAnimation() {
    	if (!isSetupMode) {
    	AnimationTimer timer = new AnimationTimer() {
    	    @Override
    	    public void handle(long now) {
    	        moveRobot();
    	        for (HardRobot hr : hardRobots) {
    	            hr.move(root.getWidth(), root.getHeight());
    	        }

    	        if (isCollidingWithCake()) {
    	            this.stop();
    	            showAlert("Congratulations", "You Won! You got the cake!");
    	        } else if (isCollidingWithHardRobots()) {
    	            this.stop();
    	            showAlert("Game Over", "You collided with a hard robot!");
    	        }
    	    }
    	};
    	
    	timer.start();
    	}
    }


    public static void main(String[] args) {
        launch(args);
    }
}
