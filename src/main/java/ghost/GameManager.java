package ghost;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;

public class GameManager {

    private Config config;
    private Map map;
    private Waka waka;
    private Fruits fruits;
    private List<Ghost> ghosts;
    private PFont gameOverFont;
    private int gameOverTimer;

    /**
     * Loads config file
     * Loads map with filename loaded from config file
     * Intialises Waka with map tiles array, speed, lives (and the letter than links it to it's position in the map)
     * Initialises Fruits with map tiles array
     * Initialises a new Ghost ArrayList
     */
    public GameManager() {
        this.config = new Config("config.json");
        this.map = new Map(config.getMapFileName());
        this.waka = new Waka(map.getTiles(), config.getSpeed(), config.getLives(), "p");
        this.fruits = new Fruits(map.getTiles());
        this.ghosts = new ArrayList<Ghost>();
        this.gameOverTimer = 0;
    }

    /**
     * @return config
     */
    public Config getConfig() {
        return this.config;
    }

    /**
     * @return map
     */
    public Map getMap() {
        return this.map;
    }

    /**
     * @return fruits
     */
    public Fruits getFruits() {
        return this.fruits;
    }

    /**
     * @return List of Ghosts
     */
    public List<Ghost> getGhosts() {
        return this.ghosts;
    }

    /**
     * Loads images for all instances
     */
    public void setup(PApplet app) {
        this.map.getImage(
            app.loadImage("src\\main\\resources\\horizontal.png"),
            app.loadImage("src\\main\\resources\\vertical.png"),
            app.loadImage("src\\main\\resources\\upLeft.png"),
            app.loadImage("src\\main\\resources\\upRight.png"),
            app.loadImage("src\\main\\resources\\downLeft.png"),
            app.loadImage("src\\main\\resources\\downRight.png")
            );
        this.waka.getImage(
            app.loadImage("src\\main\\resources\\playerClosed.png"),
            app.loadImage("src\\main\\resources\\playerDown.png"),
            app.loadImage("src\\main\\resources\\playerLeft.png"),
            app.loadImage("src\\main\\resources\\playerRight.png"),
            app.loadImage("src\\main\\resources\\playerUp.png")
            );
        this.fruits.getImage(
            app.loadImage("src\\main\\resources\\fruit.png"),
            app.loadImage("src\\main\\resources\\sodaCan.png")
            );
        this.loadGhosts(app, map.getTiles());
        this.gameOverFont = app.createFont("src\\main\\resources\\PressStart2P-Regular.ttf", 32);
    }
    
    /**
     * Add each ghost depending on the tile letter to the Ghost ArrayList after loading it's images
     */
    public void loadGhosts(PApplet app, String[][] tiles) {
        for (String[] arr : tiles) {
            for (String s : arr) {
                if (s.equals("a")) {
                    Ghost temp = new Ghost(map.getTiles(), config.getModeLengths(), config.getFrightenedLength(), config.getSpeed(), "a");
                    temp.getImage(
                        app.loadImage("src\\main\\resources\\ambusher.png"),
                        app.loadImage("src\\main\\resources\\frightened.png")
                        );
                    this.ghosts.add(temp);
                } else if (s.equals("c")) {
                    Ghost temp = new Ghost(map.getTiles(), config.getModeLengths(), config.getFrightenedLength(), config.getSpeed(), "c");
                    temp.getImage(
                        app.loadImage("src\\main\\resources\\chaser.png"),
                        app.loadImage("src\\main\\resources\\frightened.png")
                        );
                    this.ghosts.add(temp);
                } else if (s.equals("i")) {
                    Ghost temp = new Ghost(map.getTiles(), config.getModeLengths(), config.getFrightenedLength(), config.getSpeed(), "i");
                    temp.getImage(
                        app.loadImage("src\\main\\resources\\ignorant.png"),
                        app.loadImage("src\\main\\resources\\frightened.png")
                        );
                    this.ghosts.add(temp);
                } else if (s.equals("w")) {
                    Ghost temp = new Ghost(map.getTiles(), config.getModeLengths(), config.getFrightenedLength(), config.getSpeed(), "w");
                    temp.getImage(
                        app.loadImage("src\\main\\resources\\whim.png"),
                        app.loadImage("src\\main\\resources\\frightened.png")
                        );
                    this.ghosts.add(temp);
                }
            }
        }
    }

    /**
     * For each ghosts in the List Ghosts
     * If the Ghost's letter equals 'w' then loop again to search for a Ghost with the letter 'c' and update 'w' with 'c's position then break loop
     * Tick each ghost with Waka and Fruits
     */
    public void tickGhosts() {
        for (Ghost g : this.ghosts) {
            if (g.getLetter().equals("w")) {
                for (Ghost h : this.ghosts) {
                    if (h.getLetter().equals("c")) {
                        g.getChaserTile(h);
                        break;
                    }
                }
            }
            g.tick(this.waka, this.fruits);
        }
    }

    /**
     * For each Ghost in Ghost ArrayList
     * If Waka's tile center and row aligns with a Ghost's center and row
     * If Ghost was frightened, then Ghosts dies.
     * Else Waka dies and all ghosts revives
     */
    public void checkForWakasCollisionWithGhosts() {
        for (Ghost g : this.ghosts) {
            if (this.waka.getColCenter() == g.getColCenter() && this.waka.getRowCenter() == g.getRowCenter()) {
                if (g.isFrightened()) {
                    g.die();
                } else {
                    this.waka.die();
                    for (Ghost h : this.ghosts) {
                        h.revive();
                    }
                }
                return;
            }
        }
    }

    /**
     * @param app
     * If the player has won, run 'drawWinScreen' method
     * If the player has lost, run 'drawGameOverScreen' method
     * Else tick Waka, check for collision, tick all ghosts and check for collisions again in case of edge case
     * Finally tick fruits with Waka's position
     */
    public void draw(PApplet app) {
        if (this.fruits.playerHasWon()) {
            this.drawWinScreen(app);
        } else if (this.waka.hasDied()) {
            this.drawGameOverScreen(app);    
        } else {
            this.keyPressed(app);

            this.waka.tick();
            this.checkForWakasCollisionWithGhosts();
            this.tickGhosts();
            this.checkForWakasCollisionWithGhosts();
            this.fruits.tick(this.waka);

            this.map.draw(app);
            this.fruits.draw(app);
            this.drawGhosts(app);
            this.waka.draw(app); 
        }
    }
    
    /**
     * Draw each Ghost
     */
    public void drawGhosts(PApplet app) {
        for (Ghost g : this.ghosts) {
            g.draw(app);
        }
    }

    /**
     * Load game screen font
     * Increment timer with PApplet's frameCount until it reaches 10, then reset all variables to it's default settings
     */
    public void drawWinScreen(PApplet app) {
        app.textFont(gameOverFont);
        app.text("YOU WIN", App.WIDTH / 2 / 2, App.HEIGHT / 2);
        if (gameOverTimer - 1 > 10) {
            this.waka.restartGame();
            for (Ghost g : this.ghosts) {
                g.restartGame();
            }
            this.fruits.restartGame();
            gameOverTimer = 0;
        }
        if (app.frameCount % 60 == 0) {
            gameOverTimer++;
        }
    }

    /**
     * Load game screen font
     * Increment timer with PApplet's frameCount until it reaches 10, then reset all variables to it's default settings
     */
    public void drawGameOverScreen(PApplet app) {
        app.textFont(gameOverFont);
        app.text("GAME OVER", App.WIDTH / 2 / 2 - 30, App.HEIGHT / 2);
        if (gameOverTimer - 1 > 10) {
            this.waka.restartGame();
            for (Ghost g : this.ghosts) {
                g.restartGame();
            }
            this.fruits.restartGame();
            gameOverTimer = 0;
        }
        if (app.frameCount % 60 == 0) {
            gameOverTimer++;
        }
    }

    public void keyPressed(PApplet app) {
        if (app.key == App.CODED) {
            if (app.keyCode == App.LEFT) {
                this.waka.setToMoveLeft();
            } else if (app.keyCode == App.RIGHT) {
                this.waka.setToMoveRight();
            } else if (app.keyCode == App.UP) {
                this.waka.setToMoveUp();
            } else if (app.keyCode == App.DOWN) {
                this.waka.setToMoveDown();
            }
        }
    }

    /**
     * Toggles debug mode if spacebar is released
     */
    public void keyReleased(char key) {
        if (key == ' ') {
            for (Ghost g : this.ghosts) {
                g.toggleDebugMode();;
            }
        }
    }
}
