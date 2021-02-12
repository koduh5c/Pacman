package ghost;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import processing.core.PApplet;
import processing.core.PImage;

public class Map {

    private String[][] tiles;
    private PImage horizontal, vertical, upLeft, upRight, downLeft, downRight;

    /**
     * Initialise new two-dimensional String array and load the map letters
     */
    public Map(String mapFileName) {
        this.tiles = new String[36][];
        this.loadMap(mapFileName);
    }

    /**
     * @return map tiles array
     */
    public String[][] getTiles() {
        return this.tiles;
    }

    /**
     * Method for testing
     * @return A list of PImage
     */
    public List<PImage> getImageList() {
        return Arrays.asList(this.horizontal, this.vertical, this.upLeft, this.upRight, this.downLeft, this.downRight);
    }

    /**
     * Load images
     */
    public void getImage(PImage horizontal, PImage vertical, PImage upLeft, PImage upRight, PImage downLeft, PImage downRight) {
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.upLeft = upLeft;
        this.upRight = upRight;
        this.downLeft = downLeft;
        this.downRight = downRight;
    }

    /**
     * For each line map file, split the line into an array and add to the map array while incrementing the row variable at the end of each iteration
     */
    public void loadMap(String mapFileName) {
        try {
            Scanner sc = new Scanner(new File(mapFileName));
            int row = 0;
            while (sc.hasNextLine()) {
                this.tiles[row] = sc.nextLine().split("");
                row++;
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw the map
     */
    public void draw(PApplet app) {
        for (int row = 0; row < this.tiles.length; row++) {
            for (int col = 0; col < this.tiles[row].length; col++) {
                String tile = this.tiles[row][col];
                if (tile.equals("1")) {
                    app.image(this.horizontal, col * App.TILEWIDTH, row * App.TILEHEIGHT);
                } else if (tile.equals("2")) {
                    app.image(this.vertical, col * App.TILEWIDTH, row * App.TILEHEIGHT);
                } else if (tile.equals("3")) {
                    app.image(this.upLeft, col * App.TILEWIDTH, row * App.TILEHEIGHT);
                } else if (tile.equals("4")) {
                    app.image(this.upRight, col * App.TILEWIDTH, row * App.TILEHEIGHT);
                } else if (tile.equals("5")) {
                    app.image(this.downLeft, col * App.TILEWIDTH, row * App.TILEHEIGHT);
                } else if (tile.equals("6")) {
                    app.image(this.downRight, col * App.TILEWIDTH, row * App.TILEHEIGHT);
                }
            }
        }
    }
}
