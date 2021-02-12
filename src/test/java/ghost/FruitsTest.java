package ghost;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import processing.core.PImage;

public class FruitsTest {
    
    private Map horizontal;
    private Map vertical;

    @BeforeEach
    public void setUp() {
        this.horizontal = new Map("horizontal.txt");
        this.vertical = new Map("vertical.txt");
    }

    @Test
    public void testConstructor() {
        assertNotNull(new Fruits(this.horizontal.getTiles()));
        assertNotNull(new Fruits(this.vertical.getTiles()));
    }

    @Test
    public void testGetImage() {
        Fruits fruits = new Fruits(this.horizontal.getTiles());
        fruits.getImage(null, null);
        for (PImage p : fruits.getImageList()) {
            assertNull(p);
        }
    }

    @Test
    public void testCheckWhatWakaAte() {
        Fruits fruits = new Fruits(this.horizontal.getTiles());
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.tick();
        waka.setToMoveRight();
        while (waka.canGoRight) {
            waka.tick();
            fruits.tick(waka);
        }
        assertTrue(fruits.getFruits().isEmpty());
        assertTrue(fruits.getSuperFruits().isEmpty());
        assertTrue(fruits.getSodaCans().isEmpty());
        assertTrue(fruits.playerHasWon());
    }

    @Test
    public void testRestartGame() {
        Fruits fruits = new Fruits(this.horizontal.getTiles());
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.tick();
        waka.setToMoveRight();
        while (waka.canGoRight) {
            waka.tick();
            fruits.tick(waka);
        }
        fruits.restartGame();
        assertFalse(fruits.getFruits().isEmpty());
        assertFalse(fruits.getSuperFruits().isEmpty());
        assertFalse(fruits.getSodaCans().isEmpty());
    }
    

    @AfterEach
    public void tearDown() {
        this.horizontal = null;
        this.vertical = null;
    }
}
