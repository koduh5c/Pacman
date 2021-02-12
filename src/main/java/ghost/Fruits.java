package ghost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class Fruits {

    private PImage fruitImage, sodaCanImage;
    private String[][] tiles;
    private HashSet<List<Integer>> fruits, superFruits, sodaCans;
    private boolean superFruitWasEaten, sodaCanWasDrunk, playerHasWon;

    /**
     * @param tiles - String array for the map's tiles
     * Loads tiles array
     * Initialises HashSets for variables
     * Adds consumable coordinates to each relevant sets
     */
    public Fruits(String[][] tiles) {
        this.tiles = tiles;
        this.fruits = new HashSet<List<Integer>>();
        this.superFruits = new HashSet<List<Integer>>();
        this.sodaCans = new HashSet<List<Integer>>();
        this.superFruitWasEaten = false;
        this.sodaCanWasDrunk = false;
        this.playerHasWon = false;
        this.getConsumables();
    }

    /**
     * @param fruitImage - image of the fruits
     * @param sodaCanImage - image of the sodaCans
     * Loads image
     */
    public void getImage(PImage fruitImage, PImage sodaCanImage) {
        this.fruitImage = fruitImage;
        this.sodaCanImage = sodaCanImage;
    }

    /**
     * Method for testing
     * @return A list of PImage
     */
    public List<PImage> getImageList() {
        return Arrays.asList(this.fruitImage, this.sodaCanImage);
    }

    /**
     * @return set of fruits coordinates
     */
    public HashSet<List<Integer>> getFruits() {
        return this.fruits;
    }

    /**
     * @return set of superFruit coordinates
     */
    public HashSet<List<Integer>> getSuperFruits() {
        return this.superFruits;
    }

    /**
     * @return set of sodaCan coordinates
     */
    public HashSet<List<Integer>> getSodaCans() {
        return this.sodaCans;
    }

    /**
     * @return true if superFruit was eaten
     */
    public boolean superFruitWasEaten() {
        return this.superFruitWasEaten;
    }

    /**
     * @return true if sodaCan was consumed
     */
    public boolean sodaCanWasDrunk() {
        return this.sodaCanWasDrunk;
    }

    /**
     * @return true if player has won the game
     */
    public boolean playerHasWon() {
        return this.playerHasWon;
    }

    /**
     * Reads tiles array and adds each consumables coordinates to it's relevant sets
     */
    public void getConsumables() {
        for (int row = 0; row < this.tiles.length; row++) {
            for (int col = 0; col < this.tiles[row].length; col++) {
                String tile = this.tiles[row][col];
                if (tile.equals("p") || tile.equals("a") || tile.equals("i") || tile.equals("c") || tile.equals("w")
                        || tile.equals("7")) {
                    this.fruits.add(Arrays.asList(row, col));
                } else if (tile.equals("8")) {
                    this.superFruits.add(Arrays.asList(row, col));
                } else if (tile.equals("s")) {
                    this.sodaCans.add(Arrays.asList(row, col));
                }
            }
        }
    }

    /**
     * @param row - the current row of Waka
     * @param col - the current column of Waka
     * Checks which consumable Waka ate and removes it's coordinate from it's set
     * Sets booleans to true if special consumables were eaten
     */
    public void checkWhatWakaAte(int row, int col) {
        row = (int) Math.floorDiv(row + 8, App.TILEHEIGHT);
        col = (int) Math.floorDiv(col + 8, App.TILEWIDTH);
        List<Integer> currentWakaTile = new ArrayList<Integer>(Arrays.asList(row, col));
        if (this.fruits.contains(currentWakaTile)) {
            this.fruits.remove(currentWakaTile);
        }
        if (this.superFruits.contains(currentWakaTile)) {
            this.superFruits.remove(currentWakaTile);
            this.superFruitWasEaten = true;
        } else {
            this.superFruitWasEaten = false;
        }
        if (this.sodaCans.contains(currentWakaTile)) {
            this.sodaCans.remove(currentWakaTile);
            this.sodaCanWasDrunk = true;
        } else {
            this.sodaCanWasDrunk = false;
        }
    }

    /**
     * Checks if all fruits required to win the game were eaten
     */
    public void checkEatenFruits() {
        if (this.fruits.size() == 0 && this.superFruits.size() == 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.playerHasWon = true;
        }
    }

    public void tick(Waka waka) {
        this.checkEatenFruits();
        this.checkWhatWakaAte(waka.getRow(), waka.getCol());
    }

    /**
     * @param app
     */
    public void draw(PApplet app) {
        this.drawFruits(app);
        this.drawSuperFruits(app);
        this.drawSodaCans(app);
    }

    /**
     * @param app
     */
    public void drawFruits(PApplet app) {
        for (List<Integer> fruit : this.fruits) {
            app.image(this.fruitImage, fruit.get(1) * App.TILEWIDTH, fruit.get(0) * App.TILEHEIGHT);
        }
    }

    /**
     * @param app
     */
    public void drawSuperFruits(PApplet app) {
        for (List<Integer> fruit : this.superFruits) {
            app.image(this.fruitImage, fruit.get(1) * App.TILEWIDTH - 8, fruit.get(0) * App.TILEHEIGHT - 8, this.fruitImage.width * 2, this.fruitImage.height * 2);
        }
    }

    /**
     * @param app
     */
    public void drawSodaCans(PApplet app) {
        for (List<Integer> sodaCan : this.sodaCans) {
            app.image(this.sodaCanImage, sodaCan.get(1) * App.TILEWIDTH - 4, sodaCan.get(0) * App.TILEHEIGHT - 5);
        }
    }

    /**
     * Resets all setting to default
     */
    public void restartGame() {
        this.playerHasWon = false;
        this.fruits.clear();
        this.superFruits.clear();
        this.sodaCans.clear();
        this.getConsumables();
    }
}
