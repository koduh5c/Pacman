package ghost;

import processing.core.PApplet;

public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    public static final int TILEWIDTH = 16;
    public static final int TILEHEIGHT = 16;
    
    private GameManager gm;

    public App() {
        this.gm = new GameManager();
    }

    public void setup() {
        frameRate(60);
        this.gm.setup(this);
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void keyReleased() {
        this.gm.keyReleased(key);
    }

    public void draw() {
        background(0, 0, 0);
        this.gm.draw(this);
    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}
