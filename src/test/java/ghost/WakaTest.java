package ghost;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PImage;

public class WakaTest {
    
    private Map horizontal;
    private Map vertical;

    @BeforeEach
    public void setUp() {
        this.horizontal = new Map("horizontal.txt");
        this.vertical = new Map("vertical.txt");
    }

    @Test
    public void testConstructor() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertNotNull(waka);
    }

    @Test
    public void testGetLetter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals("p", waka.getLetter());
    }

    @Test
    public void testGetRow() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(26 * 16, waka.getRow());
    }

    @Test
    public void testGetRowCenter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(26 * 16 + 8, waka.getRowCenter());
    }

    @Test
    public void testGetRowTile() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(26, waka.getRowTile());
    }

    @Test
    public void testGetRowTileCenter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(26 * 16 + 8, waka.getRowTileCenter());
    }

    @Test
    public void testGetCol() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(1 * 16, waka.getCol());
    }

    @Test
    public void testGetColCenter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(1 * 16 + 8, waka.getColCenter());
    }

    @Test
    public void testGetColTile() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(1, waka.getColTile());
    }

    @Test
    public void testGetColTileCenter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(1 * 16 + 8, waka.getColTileCenter());
    }

    @Test
    public void testUpperTileCenter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertArrayEquals(new int[] {(waka.getRowTile() - 1) * 16 + 8, (waka.getColTile()) * 16 + 8}, waka.upperTileCenter());
    }

    @Test
    public void testLowerTileCenter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertArrayEquals(new int[] {(waka.getRowTile() + 1) * 16 + 8, (waka.getColTile()) * 16 + 8}, waka.lowerTileCenter());
    }

    @Test
    public void testLeftTileCenter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertArrayEquals(new int[] {(waka.getRowTile()) * 16 + 8, (waka.getColTile() - 1) * 16 + 8}, waka.leftTileCenter());
    }

    @Test
    public void testRightTileCenter() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertArrayEquals(new int[] {(waka.getRowTile()) * 16 + 8, (waka.getColTile() + 1) * 16 + 8}, waka.rightTileCenter());
    }

    @Test
    public void testUpperTileIsWalkable() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.tick();
        assertFalse(waka.upperTileIsWalkable());
    }

    @Test
    public void testLowerTileIsWalkable() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.tick();
        assertFalse(waka.lowerTileIsWalkable());
    }

    @Test
    public void testLeftTileIsWalkable() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.tick();
        assertFalse(waka.leftTileIsWalkable());
    }

    @Test
    public void testRightTileIsWalkable() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.tick();
        assertTrue(waka.rightTileIsWalkable());     
    }

    @Test
    public void testResetPosition() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.tick();
        waka.setToMoveRight();
        waka.tick();
        assertNotEquals(1 * 16, waka.getCol());
        waka.resetPosition();
        assertEquals(1 * 16, waka.getCol());
    }

    @Test
    public void testGetImage() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.getImage(null, null, null, null, null);
        for (PImage p : waka.getImageList()) {
            assertNull(p);
        }
    }

    @Test
    public void testGetLivesLeft() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        assertEquals(3, waka.getLivesLeft());
    }

    @Test
    public void testIsMovingHorizontally() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.tick();
        waka.setToMoveRight();
        assertTrue('R' == waka.isMoving());
        waka.tick();
        waka.setToMoveLeft();
        assertTrue('L' == waka.isMoving());
    }

    @Test
    public void testIsMovingVertically() {
        Waka waka = new Waka(this.vertical.getTiles(), 1, 3, "p");
        waka.tick();
        waka.setToMoveUp();
        assertTrue('U' == waka.isMoving());
        waka.tick();
        waka.setToMoveDown();
        assertTrue('D' == waka.isMoving());
    }

    @Test
    public void testDieAndRevive() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.die();
        assertEquals(2, waka.getLivesLeft());
        waka.die();
        assertEquals(1, waka.getLivesLeft());
        waka.die();
        assertTrue(waka.hasDied());
        waka.revive();
        assertFalse(waka.hasDied());
    }

    @Test
    public void testRestartGame() {
        Waka waka = new Waka(this.horizontal.getTiles(), 1, 3, "p");
        waka.die();
        assertEquals(2, waka.getLivesLeft());
        waka.die();
        assertEquals(1, waka.getLivesLeft());
        waka.die();
        assertTrue(waka.hasDied());
        waka.restartGame();
        assertEquals(3, waka.getLivesLeft());
        assertFalse(waka.hasDied());
    }

    @AfterEach
    public void tearDown() {
        this.horizontal = null;
        this.vertical = null;
    }
}
