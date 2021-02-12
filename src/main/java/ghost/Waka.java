package ghost;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class Waka extends Movable {

    private PImage playerClosed, playerDown, playerLeft, playerRight, playerUp, currentImage;
    private char isMoving;
    private int defaultLives, livesLeft;

    /**
     * Sets map array tiles, speeed, livesLeft and the letter of Waka
     * Sets it's initial moving direction to the left
     */
    public Waka(String[][] tiles, int speed, int livesLeft, String letter) {
        super(tiles, speed, letter);
        this.defaultLives = livesLeft;
        this.livesLeft = livesLeft;
        this.tick();
        this.setToMoveLeft();
    }

    /**
     * Loads images
     */
    public void getImage(PImage playerClosed, PImage playerDown, PImage playerLeft, PImage playerRight,
            PImage playerUp) {
        this.playerClosed = playerClosed;
        this.playerDown = playerDown;
        this.playerLeft = playerLeft;
        this.playerRight = playerRight;
        this.playerUp = playerUp;

        this.currentImage = this.playerLeft;
    }

    /**
     * Method for testing
     * @return A list of PImage
     */
    public List<PImage> getImageList() {
        return Arrays.asList(this.playerClosed, this.playerDown, this.playerLeft, this.playerRight, this.playerUp);
    }

    /**
     * @return livesLeft
     */
    public int getLivesLeft() {
        return this.livesLeft;
    }

    /**
     * Set isAlive to true and reset position
     */
    public void revive() {
        this.isAlive = true;
        this.resetPosition();
    }

    /**
     * @return the character of the current moving direction
     */
    public char isMoving() {
        return this.isMoving;
    }

    /**
     * Sets Waka to move in it's relevant direction if condition is satisfied
     */
    public void setToMoveUp() {
        if (this.canGoUp) {
            this.isMoving = 'U';
        }
    }

    /**
     * Sets Waka to move in it's relevant direction if condition is satisfied
     */
    public void setToMoveDown() {
        if (this.canGoDown) {
            this.isMoving = 'D';
        }
    }

    /**
     * Sets Waka to move in it's relevant direction if condition is satisfied
     */
    public void setToMoveLeft() {
        if (this.canGoLeft) {
            this.isMoving = 'L';
        }
    }

    /**
     * Sets Waka to move in it's relevant direction if condition is satisfied
     */
    public void setToMoveRight() {
        if (this.canGoRight) {
            this.isMoving = 'R';
        }
    }

    /**
     * Moves the currentPosition of Waka in the direction commanded to move towards
     */
    public void move() {
        if (this.isMoving == 'U' && this.canGoUp) {
            this.currentImage = this.playerUp;
            this.currentPosition[0] -= this.speed;
        } else if (this.isMoving == 'D' && this.canGoDown) {
            this.currentImage = this.playerDown;
            this.currentPosition[0] += this.speed;
        } else if (this.isMoving == 'L' && this.canGoLeft) {
            this.currentImage = this.playerLeft;
            this.currentPosition[1] -= this.speed;
        } else if (this.isMoving == 'R' && this.canGoRight) {
            this.currentImage = this.playerRight;
            this.currentPosition[1] += this.speed;
        }
    }

    /**
     * Sleep thread for 500ms for a sense of impact
     * If the number of lives left are more than one, decrement it's value by one, else set isAlive boolean to false and reset the currentPosition
     * If the last moving direction was either up or down, push position back by one pixel in it's opposite direction to assure correct gameplay
     * Finally tick Waka and set to move left
     */
    public void die() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.livesLeft > 1) {
            this.livesLeft--;
        } else {
            this.isAlive = false;
        }
        this.resetPosition();
        if (this.isMoving == 'U') {
            this.currentPosition[0] += 1;
        } else if (this.isMoving == 'D') {
            this.currentPosition[0] -= 1;
        }
        this.tick();
        this.setToMoveLeft();
    }

    /**
     * Set moving direction and check wall collisions
     */
    public void tick() {
        this.move();
        this.checkWallCollisions();
    }

    public void draw(PApplet app) {
        this.drawWaka(app);
        this.drawLives(app);
    }

    public void drawWaka(PApplet app) {
        if (app.frameCount % 50 > 25) {
            app.image(this.playerClosed, this.currentPosition[1] - 4, this.currentPosition[0] - 5);
        } else {
            app.image(this.currentImage, this.currentPosition[1] - 4, this.currentPosition[0] - 5);
        }
    }

    /**
     * Keep reading the number of lives left and draw the corresponding count of images in the bottom left corner to display it's remaining lives
     */
    public void drawLives(PApplet app) {
        int width = 5;
        int height = App.HEIGHT - this.playerRight.pixelHeight - 5;
        for (int i = 0; i < this.livesLeft; i++) {
            app.image(this.playerRight, width, height);
            width += this.playerRight.pixelWidth + 5;
        }
    }
    
    /**
     * Reset all variables to it's default settings
     */
    public void restartGame() {
        this.resetPosition();
        this.setToMoveLeft();
        this.livesLeft = this.defaultLives;
        this.revive();
        this.tick();
    }
}
