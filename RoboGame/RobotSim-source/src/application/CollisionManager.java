//package application;
//
//import java.util.List;
//
//import javafx.scene.layout.GridPane;
//public class CollisionManager {
//    private List<ConsoleItem> consoleItems;
//    private int arenaWidth;
//    private int arenaHeight;
//
//    public CollisionManager(List<ConsoleItem> consoleItems, int width, int height) {
//        this.consoleItems = consoleItems;
//        this.arenaWidth = width;
//        this.arenaHeight = height;
//    }
//
//    public void checkCollisions() {
//        // Check for collisions between objects
//        for (int i = 0; i < consoleItems.size(); i++) {
//            ConsoleItem item1 = consoleItems.get(i);
//            for (int j = i + 1; j < consoleItems.size(); j++) {
//                ConsoleItem item2 = consoleItems.get(j);
//                if (item1.getPosX() == item2.getPosX() && item1.getPosY() == item2.getPosY()) {
//                    handleCollision(item1, item2);
//                }
//            }
//            // Check for collision with arena borders
//            checkBorderCollision(item1);
//        }
//    }
//
//    private void handleCollision(ConsoleItem item1, ConsoleItem item2) {
//        if (item1 instanceof SoftRobot && item2 instanceof HardRobot) {
//            consoleItems.remove(item1); // Remove the SoftRobot
//            ((HardRobot) item2).continueMovement(); // HardRobot continues movement
//        } else if (item1 instanceof HardRobot && item2 instanceof SoftRobot) {
//            consoleItems.remove(item2); // Remove the SoftRobot
//            ((HardRobot) item1).continueMovement(); // HardRobot continues movement
//        } else if (item1 instanceof SoftRobot && item2 instanceof SoftRobot) {
//            ((SoftRobot) item1).bounceOff((SoftRobot) item2); // Both SoftRobots bounce off each other
//        } else if (item1 instanceof HardRobot && item2 instanceof HardRobot) {
//            ((HardRobot) item1).bounceOff((HardRobot) item2); // Both HardRobots bounce off each other
//        } else if (item1 instanceof Wall) {
//            handleWallCollision(item2); // Handle collision with a Wall for item2
//        } else if (item2 instanceof Wall) {
//            handleWallCollision(item1); // Handle collision with a Wall for item1
//        }
//        // Add other collision handling as needed
//    }
//
//    private void handleWallCollision(ConsoleItem item) {
//        if (item instanceof HardRobot) {
//            ((HardRobot) item).bounceOffWall();
//        } else if (item instanceof SoftRobot) {
//            ((SoftRobot) item).bounceOffWall();
//        }
//        // Add similar conditions for any other ConsoleItem types that can bounce off walls
//    }
//    
//    private void checkBorderCollision(ConsoleItem item) {
//        boolean isAtBorder = item.getPosX() == 0 || item.getPosX() == arenaWidth - 1 || item.getPosY() == 0 || item.getPosY() == arenaHeight - 1;
//
//        if (isAtBorder) {
//            if (item instanceof HardRobot) {
//                ((HardRobot) item).bounceOffWall();
//            } else if (item instanceof SoftRobot) {
//                ((SoftRobot) item).bounceOffWall();
//            }
//            // Add similar conditions for any other ConsoleItem types that can bounce off walls
//        }
//    }
//
//}
