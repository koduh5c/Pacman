package ghost;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;

import processing.core.PImage;

public class GhostTest {
    
    private Map map;
    private Waka waka;
    private Config config;
    private Fruits fruits;
    private Ghost ambusher, chaser, ignorant, whim;
    private List<Ghost> ghosts;

    @BeforeEach
    public void setUp() {
        this.config = new Config("config.json");
        this.map = new Map("vertical.txt");
        this.waka = new Waka(this.map.getTiles(), this.config.getSpeed(), this.config.getSpeed(), "p");
        this.fruits = new Fruits(this.map.getTiles());
        this.ambusher = new Ghost(this.map.getTiles(), this.config.getModeLengths(), this.config.getFrightenedLength(), this.config.getSpeed(), "a");
        this.chaser = new Ghost(this.map.getTiles(), this.config.getModeLengths(), this.config.getFrightenedLength(), this.config.getSpeed(), "c");
        this.ignorant = new Ghost(this.map.getTiles(), this.config.getModeLengths(), this.config.getFrightenedLength(), this.config.getSpeed(), "i");
        this.whim = new Ghost(this.map.getTiles(), this.config.getModeLengths(), this.config.getFrightenedLength(), this.config.getSpeed(), "w");
        this.ghosts = Arrays.asList(ambusher, chaser, ignorant, whim);
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.ghosts);
        for (Ghost g : this.ghosts) {
            assertNotNull(g);
        }
    }

    @Test
    public void testGetImage() {
        for (Ghost g : this.ghosts) {
            for (PImage p : g.getImageList()) {
                assertNull(p);
            }
        }
    }

    @Test
    public void testWakaKillsAllGhosts() {
        this.waka.tick();
        this.waka.setToMoveDown();
        while (this.waka.canGoDown) {
            this.waka.tick();
            this.chaser.tick(this.waka, this.fruits);
            this.fruits.tick(this.waka);
        }
        this.waka.setToMoveUp();
        while (this.waka.canGoUp) {
            this.waka.tick();
            this.chaser.tick(this.waka, this.fruits);
            this.fruits.tick(this.waka);
        }
        for (Ghost g : this.ghosts) {
            assertTrue(g.hasDied());
        }
    }

    @AfterEach
    public void tearDown() {
        this.config = null;
        this.map = null;
        this.waka = null;
        this.fruits = null;
        this.ghosts = null;
    }
}
