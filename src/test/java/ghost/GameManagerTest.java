package ghost;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class GameManagerTest {
    
    private GameManager gm;

    @BeforeEach
    public void setUp() {
        this.gm = new GameManager();
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.gm);
        assertNotNull(this.gm.getConfig());
        assertNotNull(this.gm.getMap());
        assertNotNull(this.gm.getFruits());
        assertNotNull(this.gm.getGhosts());
    }

    @AfterEach
    public void tearDown() {
        this.gm = null;
    }
}
