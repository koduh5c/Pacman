package ghost;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PImage;

public class MapTest {
    
    private Map map;

    @BeforeEach
    public void setUp() {
        this.map = new Map("map.txt");
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.map);
    }

    @Test
    public void testGetTiles() {
        assertNotNull(this.map.getTiles());
    }

    @Test
    public void testGetImage() {
        this.map.getImage(null, null, null, null, null, null);
        for (PImage p : this.map.getImageList()) {
            assertNull(p);
        }
    }

    @AfterEach
    public void tearDown() {
        this.map = null;
    }
}
