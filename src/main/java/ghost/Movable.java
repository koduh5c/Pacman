package ghost;

import processing.core.PApplet;

abstract class Movable {

    protected boolean canGoUp, canGoDown, canGoLeft, canGoRight;
    protected String[][] tiles;
    protected String[] walkableLetters;
    protected int speed;
    protected int[] currentPosition, startPosition;
    protected String letter;
    protected boolean isAlive;

    /**
     * Set's map array tiles, speed and the letter for the Movable instance
     * Initialises the walkable letters for the instance (Further characters can be added later if required)
     * Get the starting position of the instance using the it's letter
     */
    public Movable(String[][] tiles, int speed, String letter) {
        this.tiles = tiles;
        this.speed = speed;
        this.letter = letter;
        this.canGoUp = false;
        this.canGoDown = false;
        this.canGoLeft = false;
        this.canGoRight = false;
        this.isAlive = true;
        this.walkableLetters = new String[] {"p", "a", "i", "c", "w", "7", "8", "s", "0"};
        this.getStartPosition(letter);
    }

    /**
     * All Movable instances are required to move
     */
    public abstract void move();

    /**
     * All Movable instances are required to draw
     */
    public abstract void draw(PApplet app);

    /**
     * All Movable instances are required to reset it's settings
     */
    public abstract void restartGame();

    /**
     * If this isAlive is false, then return true
     */
    public boolean hasDied() {
        if (this.isAlive) {
            return false;
        }
        return true;
    }

    /**
     * @return letter
     */
    public String getLetter() {
        return this.letter;
    }
    
    /**
     * @return the row position
     */
    public int getRow() {
        return this.currentPosition[0];
    }

    /**
     * @return this row's center position
     */
    public int getRowCenter() {
        return this.currentPosition[0] + 8;
    }

    /**
     * @return this row's tiles number
     */
    public int getRowTile() {
        return (int) Math.floorDiv(currentPosition[0], 16);
    }

    /**
     * @return this row's tile's center position
     */
    public int getRowTileCenter() {
        return this.getRowTile() * 16 + 8;
    }

    /**
     * @return the column position
     */
    public int getCol() {
        return this.currentPosition[1];
    }

    /**
     * @return this column's center position
     */
    public int getColCenter() {
        return this.currentPosition[1] + 8;
    }

    /**
     * @return this column's tiles number
     */
    public int getColTile() {
        return (int) Math.floorDiv(currentPosition[1], 16);
    }
    
    /**
     * @return this column's tile's center position
     */
    public int getColTileCenter() {
        return this.getColTile() * 16 + 8;
    }

    /**
     * @return the upper tile's center position of this instance
     */
    public int[] upperTileCenter() {
        return new int[] {(this.getRowTile() - 1) * 16 + 8, this.getColTile() * 16 + 8};
    }

    /**
     * @return the lower tiles's center position of this instance
     */
    public int[] lowerTileCenter() {
        return new int[] {(this.getRowTile() + 1) * 16 + 8, this.getColTile() * 16 + 8};
    }

    /**
     * @return the left tiles's center position of this instance
     */
    public int[] leftTileCenter() {
        return new int[] {this.getRowTile() * 16 + 8, (this.getColTile() - 1) * 16 + 8};
    }

    /**
     * @return the right tiles's center position of this instance
     */
    public int[] rightTileCenter() {
        return new int[] {this.getRowTile() * 16 + 8, (this.getColTile() + 1) * 16 + 8};
    }

    /**
     * @return true if relevant tile is walkable after checking the tile using the isWalkable method
     */
    public boolean upperTileIsWalkable() {
        return this.isWalkable(this.currentPosition[0] - 1, this.currentPosition[1]) && this.isWalkable(this.currentPosition[0] - 1, this.currentPosition[1] + 15);
    }

    /**
     * @return true if relevant tile is walkable after checking the tile using the isWalkable method
     */
    public boolean lowerTileIsWalkable() {
        return this.isWalkable(this.currentPosition[0] + 16, this.currentPosition[1]) && this.isWalkable(this.currentPosition[0] + 16, this.currentPosition[1] + 15);
    }

    /**
     * @return true if relevant tile is walkable after checking the tile using the isWalkable method
     */
    public boolean leftTileIsWalkable() {
        return this.isWalkable(this.currentPosition[0], this.currentPosition[1] - 1) && this.isWalkable(this.currentPosition[0] + 15, this.currentPosition[1] - 1);
    }

    /**
     * @return true if relevant tile is walkable after checking the tile using the isWalkable method
     */
    public boolean rightTileIsWalkable() {
        return this.isWalkable(this.currentPosition[0], this.currentPosition[1] + 16) && this.isWalkable(this.currentPosition[0] + 15, this.currentPosition[1] + 16);
    }

    /**
     * Sets relevant booleans depending on if it's relevant direction was walkable or not
     */
    public void checkWallCollisions() {
        if (this.upperTileIsWalkable()) {
            this.canGoUp = true;
        } else {
            this.canGoUp = false;
        }
        if (this.lowerTileIsWalkable()) {
            this.canGoDown = true;
        } else {
            this.canGoDown = false;
        }
        if (this.leftTileIsWalkable()) {
            this.canGoLeft = true;
        } else {
            this.canGoLeft = false;
        }
        if (this.rightTileIsWalkable()) {
            this.canGoRight = true;
        } else {
            this.canGoRight = false;
        }
    }

    /**
     * Sets the starting position of this instance
     */
    public void getStartPosition(String s) {
        for (int row = 0; row < this.tiles.length; row++) {
            for (int col = 0; col < this.tiles[row].length; col++) {
                if (this.tiles[row][col].equals(s)) {
                    this.currentPosition = new int[] {row * App.TILEHEIGHT, col * App.TILEWIDTH};
                    this.startPosition = new int[] {row * App.TILEHEIGHT, col * App.TILEWIDTH};
                    return;
                }
            }
        }
    }

    /**
     * Checks if the given tile is walkable
     */
    public boolean isWalkable(int row, int col) {
        row = (int) Math.floorDiv(row, App.TILEHEIGHT);
        col = (int) Math.floorDiv(col, App.TILEWIDTH);
        String tile = this.tiles[row][col];
        for (String letter : this.walkableLetters) {
            if (letter.equals(tile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resets the position of the instance to it's default starting position
     */
    public void resetPosition() {
        this.currentPosition[0] = this.startPosition[0];
        this.currentPosition[1] = this.startPosition[1];
    }

}
