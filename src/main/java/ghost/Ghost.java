package ghost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class Ghost extends Movable {

    private PImage ghostImage, frightenedImage;
    private List<Integer> modeLengths;
    private int modeCursor;
    private int modeTimer;
    private int frightenedLength;
    private int frightenedTimer;
    private char lastMove;
    private boolean isChasing, isFrightened, isInSodaCanMode, isInDebugMode;
    private int[] target, chaserTile;

    /**
     * Loads map tiles array, List of modeLengths, frightenedLength, speed, letter
     * Sets target to the Ghost's nearest corner
     */
    public Ghost(String[][] tiles, List<Integer> modeLengths, int frightenedLength, int speed, String letter) {
        super(tiles, speed, letter);
        this.modeCursor = 0;
        this.modeTimer = 0;
        this.frightenedTimer = 0;
        this.lastMove = ' ';
        this.isChasing = false;
        this.isFrightened = false;
        this.isInSodaCanMode = false;
        this.isInDebugMode = false;
        this.target = new int[] {0, 0};
        this.chaserTile = new int[] {0, 0};
        this.frightenedLength = frightenedLength;
        this.modeLengths = modeLengths;
        this.setTargetToNearestCorner();
    }

    /**
     * Loads images
     */
    public void getImage(PImage ghostImage, PImage frightenedImage) {
        this.ghostImage = ghostImage;
        this.frightenedImage = frightenedImage;
    }

    /**
     * Method for testing
     * @return A list of PImage
     */
    public List<PImage> getImageList() {
        return Arrays.asList(this.ghostImage, this.frightenedImage);
    }

    /**
     * Toggles debug mode
     */
    public void toggleDebugMode() {
        this.isInDebugMode = !this.isInDebugMode;
    }

    /**
     * Sleeps for 100ms for a sense of impact, sets boolean to false and sets currentPosition to top left corner
     */
    public void die() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.isAlive = false;
        this.currentPosition[0] = 0;
        this.currentPosition[1] = 0;
    }

    /**
     * Revives Ghost and resets positions to default
     */
    public void revive() {
        this.isAlive = true;
        this.resetPosition();
    }

    /**
     * Sets last move to it's relevant char and moves currentPosition of Ghost
     */
    public void moveUp() {
        this.currentPosition[0] -= this.speed;
        this.lastMove = 'U';
    }

    /**
     * Sets last move to it's relevant char and moves currentPosition of Ghost
     */
    public void moveLeft() {
        this.currentPosition[1] -= this.speed;
        this.lastMove = 'L';
    }

    /**
     * Sets last move to it's relevant char and moves currentPosition of Ghost
     */
    public void moveRight() {
        this.currentPosition[1] += this.speed;
        this.lastMove = 'R';
    }

    /**
     * Sets last move to it's relevant char and moves currentPosition of Ghost
     */
    public void moveDown() {
        this.currentPosition[0] += this.speed;
        this.lastMove = 'D';
    }

    /**
     * Calculates the Euclidean distance from point a to point b
     */
    public double getDistance(int rowFrom, int colFrom, int rowTo, int colTo) {
        return Math.sqrt((rowFrom - rowTo) * (rowFrom - rowTo) + (colFrom - colTo) * (colFrom - colTo));
    }

    /**
     * @return true if frightened
     */
    public boolean isFrightened() {
        return this.isFrightened;
    }

    /**
     * Sets direction of the Ghost after comparing distance value and it's required conditions
     */
    public void setDirection(double choice, double up, double left, double right, double down) {
        if (this.canGoUp && choice == up && this.lastMove != 'D') {
            this.moveUp();
        } else if (this.canGoLeft && choice == left && this.lastMove != 'R') {
            this.moveLeft();
        } else if (this.canGoRight && choice == right && this.lastMove != 'L') {
            this.moveRight();
        } else if (this.canGoDown && choice == down && this.lastMove != 'U') {
            this.moveDown();
        }
    }

    /**
     * If the Ghost is at an intersection (more than 3 movable directions)
     * If it is frightened, initialise a random class, a random number from 0 - 4 and use setDirections method to set the direction
     * Else initialise a Doubles list and depending on the directions the Ghost is able to move towards, add the distance from one tile in that direction to the target to the list
     * Skip any direction that cannot be moved towards
     * Set the direction using the setDirections method
     * 
     * If the Ghost can only move in 2 directions, then continue moving in the direction it was moving in except the last direction using 'D', 'L', 'R', 'U' char's as it's marker.
     * 
     * If the the Ghost is at a dead end, simply move in the direction that is walkable.
     */
    public void move() {
        int count = this.getMovableDirectionCount();
        if (count > 2) {
            if (this.isFrightened) {
                Random rand = new Random();
                double randomNum = (double) rand.nextInt(4);
                this.setDirection(randomNum, 0, 1, 2, 3);
            } else {
                List<Double> list = new ArrayList<Double>();
                double distFromUpperTile = 0, distFromLowerTile = 0, distFromLeftTile = 0, distFromRightTile = 0;
                if (this.canGoUp && this.lastMove != 'D') {
                    distFromUpperTile = this.getDistance(this.upperTileCenter()[0], this.upperTileCenter()[1], this.target[0] + 8, this.target[1] + 8);
                    list.add(distFromUpperTile);
                }
                if (this.canGoLeft && this.lastMove != 'R') {
                    distFromLeftTile = this.getDistance(this.leftTileCenter()[0], this.leftTileCenter()[1], this.target[0] + 8, this.target[1] + 8);
                    list.add(distFromLeftTile);
                }
                if (this.canGoRight && this.lastMove != 'L') {
                    distFromRightTile = this.getDistance(this.rightTileCenter()[0], this.rightTileCenter()[1], this.target[0] + 8, this.target[1] + 8);
                    list.add(distFromRightTile);
                }
                if (this.canGoDown && this.lastMove != 'U') {
                    distFromLowerTile = this.getDistance(this.lowerTileCenter()[0], this.lowerTileCenter()[1], this.target[0] + 8, this.target[1] + 8);
                    list.add(distFromLowerTile);
                }
                double fastestDirection = Collections.min(list);
                this.setDirection(fastestDirection, distFromUpperTile, distFromLeftTile, distFromRightTile, distFromLowerTile);
            }
        } else if (count == 2) {
            if (this.canGoUp && this.lastMove != 'D') {
                this.moveUp();
            } else if (this.canGoRight && this.lastMove != 'L') {
                this.moveRight();
            } else if (this.canGoLeft && this.lastMove != 'R') {
                this.moveLeft();
            } else if (this.canGoDown && this.lastMove != 'U') {
                this.moveDown();
            }
        } else if (count == 1) {
            if (this.canGoUp) {
                this.moveUp();
            } else if (this.canGoRight) {
                this.moveRight();
            } else if (this.canGoLeft) {
                this.moveLeft();
            } else if (this.canGoDown) {
                this.moveDown();
            }
        }
    }

    /**
     * @return the number of directions that is walkable.
     */
    public int getMovableDirectionCount() {
        int counter = 0;
        if (this.canGoUp) {
            counter++;
        }
        if (this.canGoLeft) {
            counter++;
        }
        if (this.canGoRight) {
            counter++;
        }
        if (this.canGoDown) {
            counter++;
        }
        return counter;
    }

    /**
     * Sets Chaser ghost's target
     */
    public void setChaserTarget(Waka waka) {
        this.target[0] = waka.getRowTileCenter();
        this.target[1] = waka.getColTileCenter();
    }

    /**
     * Sets Ambusher ghost's target
     */
    public void setAmbusherTarget(Waka waka) {
        if (waka.isMoving() == 'U') {
            this.target[0] = waka.getRowTileCenter() - 16 * 4;
            this.target[1] = waka.getColTileCenter();
        } else if (waka.isMoving() == 'D') {
            this.target[0] = waka.getRowTileCenter() + 16 * 4;
            this.target[1] = waka.getColTileCenter();
        } else if (waka.isMoving() == 'L') {
            this.target[0] = waka.getRowTileCenter();
            this.target[1] = waka.getColTileCenter() - 16 * 4;
        } else if (waka.isMoving() == 'R') {
            this.target[0] = waka.getRowTileCenter();
            this.target[1] = waka.getColTileCenter() + 16 * 4;
        }
    }
    
    /**
     * Sets Ignorant ghost's target
     */
    public void setIgnorantTarget(Waka waka) {
        int distanceFromWaka = (int) this.getDistance(this.getRowTileCenter(), this.getColTileCenter(), waka.getRowTileCenter(), waka.getColTileCenter()) / 16;
        if (distanceFromWaka > 8) {
            this.target[0] = waka.getRowTileCenter();
            this.target[1] = waka.getColTileCenter();
        } else {
            this.target[0] = App.HEIGHT;
            this.target[1] = 0;
        }  
    }
    
    /**
     * Sets Whim ghost's target
     */
    public void setWhimTarget(Waka waka) {
        int tempRowTile = 0;
        int tempColTile = 0;
        if (waka.isMoving() == 'U') {
            tempRowTile = waka.getRowTile() - 2;
            tempColTile = waka.getColTile();
        } else if (waka.isMoving() == 'D') {
            tempRowTile = waka.getRowTile() + 2;
            tempColTile = waka.getColTile();
        } else if (waka.isMoving() == 'L') {
            tempRowTile = waka.getRowTile();
            tempColTile = waka.getColTile() - 2;
        } else if (waka.isMoving() == 'R') {
            tempRowTile = waka.getRowTile();
            tempColTile = waka.getColTile() + 2;
        }
        this.target[0] = (this.chaserTile[0] + 2 * (tempRowTile - this.chaserTile[0])) * 16 + 8;
        this.target[1] = (this.chaserTile[1] + 2 * (tempColTile - this.chaserTile[1])) * 16 + 8;
    }
    
    /**
     * Sets target to Waka
     */
    public void setTargetToWaka(Waka waka) {
        if (this.letter.equals("c")) {
            this.setChaserTarget(waka);
        } else if (this.letter.equals("a")) {
            this.setAmbusherTarget(waka);
        } else if (this.letter.equals("i")) {
            this.setIgnorantTarget(waka);
        } else if (this.letter.equals("w")) {
            this.setWhimTarget(waka);
        }
    }

    /**
     * Sets target to it's nearest corner
     */
    public void setTargetToNearestCorner() {
        if (this.letter.equals("c")) {
            this.target[0] = 0;
            this.target[1] = 0;
        } else if (this.letter.equals("a")) {
            this.target[0] = 0;
            this.target[1] = App.WIDTH; 
        } else if (this.letter.equals("i")) {
            this.target[0] = App.HEIGHT;
            this.target[1] = 0;
        } else if (this.letter.equals("w")) {
            this.target[0] = App.HEIGHT;
            this.target[1] = App.WIDTH; 
        }
    }

    /**
     * If frightened and the frightened timer is up, set isFrightened and isInSodaCan boolean to false and reset timer
     * 
     * Else, if modeTimer is up, increment the modeCursor and reset the timer
     * If the modeCursor is even, then the Ghost should get it's nearest corner and scatter, else it should be chasing Waka.
     */
    public void setMode() {
        if (this.isFrightened) {
            if (this.frightenedLength - 1 < this.frightenedTimer) {
                this.isFrightened = false;
                this.isInSodaCanMode = false;
                this.frightenedTimer = 0;
            }
        } else {
            if (this.modeLengths.get(this.modeCursor) - 1 < this.modeTimer) {
                this.modeCursor = (this.modeCursor + 1) % this.modeLengths.size();
                this.modeTimer = 0;
                if (this.modeCursor % 2 == 0) {
                    this.isChasing = false;
                    this.setTargetToNearestCorner();
                } else {
                    this.isChasing = true;
                }
            }
        }
    }

    /**
     * Increment timer depending on the current mode
     */
    public void incrementTimer(PApplet app) {
        if (!this.isFrightened) {
            if (app.frameCount % 60 == 0) {
                this.modeTimer++;
            }
        } else {
            if (app.frameCount % 60 == 0) {
                this.frightenedTimer++;
            }
        }
    }

    /**
     * Update the Chaser ghost's position to calculate Whim's target
     */
    public void getChaserTile(Ghost chaser) {
        this.chaserTile[0] = chaser.getRowTile();
        this.chaserTile[1] = chaser.getColTile();
    }

    /**
     * Check if a special consumable was eaten,
     * If superFruit was eaten,set isFrightened boolean to true
     * Else if a sodaCan was drunk
     * Set both sodaCan and frightened booleans to true
     */
    public void checkIfSpecialWasEaten(Fruits fruits) {
        if (fruits.superFruitWasEaten()) {
            this.isFrightened = true;
        } else if (fruits.sodaCanWasDrunk()) {
            this.isFrightened = true;
            this.isInSodaCanMode = true;
        }
    }

    /**
     * First, setMode()
     * And if this Ghost is alive, check if special consumable was eaten.
     * If this is chasing and not frightened, keep getting Waka's position and set the target
     * Finally check wall collisions and run move logic
     */
    public void tick(Waka waka, Fruits fruits) {
        this.setMode();
        if (this.isAlive) {
            this.checkIfSpecialWasEaten(fruits);
            if (this.isChasing && !this.isFrightened) {
                this.setTargetToWaka(waka);
            }
            this.checkWallCollisions();
            this.move();
        }
    }

    /**
     * If this ghost is alive
     * If this is in debug mode, draw the debug lines
     * Draw the ghost
     * And increment the timer
     */
    public void draw(PApplet app) {
        if (this.isAlive) {
            if (this.isInDebugMode) {
                this.drawDebugModeLine(app);
            }
            this.drawGhost(app);
        }
        this.incrementTimer(app);
    }

    /**
     * If ghost is frightened and is not in sodaCan mode, draw the frightened image
     * Else if is frightened and in sodaCan mode, draw the image once every second
     * 
     * If all booleans are false, simply draw the ghost
     */
    public void drawGhost(PApplet app) {
        if (this.isFrightened && !this.isInSodaCanMode) {
            app.image(this.frightenedImage, this.getCol() - 4, this.getRow() - 5);
        } else if (this.isFrightened && this.isInSodaCanMode) {
            if (app.frameCount % 60 == 0) {
                app.image(this.frightenedImage, this.getCol() - 4, this.getRow() - 5);    
            }
        } else {
            app.image(this.ghostImage, this.getCol() - 4, this.getRow() - 5);
        }    
    }

    /**
     * If this is not frightened, draw a straight white line to the current target's position
     */
    public void drawDebugModeLine(PApplet app) {
        if (!this.isFrightened) {
            app.stroke(255);
            app.line(this.getColCenter(), this.getRowCenter(), this.target[1], this.target[0]);
        }
    }

    /**
     * Reset all variables to it's default settings
     */
    public void restartGame() {
        this.modeCursor = 0;
        this.modeTimer = 0;
        this.frightenedTimer = 0;
        this.lastMove = ' ';
        this.isChasing = false;
        this.isFrightened = false;
        this.setTargetToNearestCorner();
    }
    
}
