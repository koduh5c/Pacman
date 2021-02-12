package ghost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfigTest {
    
    private Config config;

    @BeforeEach
    public void setUp() {
        this.config = new Config("config.json");
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.config);
    }

    @Test
    public void testGetMapFileName() {
        assertEquals("map.txt", this.config.getMapFileName());
    }

    @Test
    public void testGetLives() {
        assertEquals(3, this.config.getLives());
    }

    @Test
    public void testGetSpeed() {
        assertEquals(1, this.config.getSpeed());
    }

    @Test
    public void testGetFrightenedLength() {
        assertEquals(5, this.config.getFrightenedLength());
    }

    @Test
    public void testGetModeLengths() {
        assertEquals(Arrays.asList(7, 20, 7, 20, 5, 20, 5, 1000), this.config.getModeLengths());
    }

    @AfterEach
    public void tearDown() {
        this.config = null;
    }
}
